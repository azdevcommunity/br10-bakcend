package fib.br10.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fib.br10.core.entity.EntityStatus;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.DateUtil;
import fib.br10.dto.history.customer.response.CustomerHistoryDetailsDTO;
import fib.br10.dto.history.customer.response.CustomerHistoryResponse;
import fib.br10.dto.history.customer.response.ServiceResponseDTO;
import fib.br10.dto.notification.PushNotificationRequest;
import fib.br10.dto.reservation.request.CancelReservationRequest;
import fib.br10.dto.reservation.request.CreateReservationRequest;
import fib.br10.dto.reservation.request.UpdateReservationRequest;
import fib.br10.dto.reservation.response.ReservationDetailResponse;
import fib.br10.dto.reservation.response.ReservationResponse;
import fib.br10.entity.QImage;
import fib.br10.entity.reservation.QReservation;
import fib.br10.entity.reservation.QReservationDetail;
import fib.br10.entity.reservation.Reservation;
import fib.br10.entity.reservation.ReservationDetail;
import fib.br10.entity.reservation.ReservationSource;
import fib.br10.entity.reservation.ReservationStatus;
import fib.br10.entity.specialist.QSpecialistService;
import fib.br10.entity.specialist.SpecialistService;
import fib.br10.entity.user.QUser;
import fib.br10.entity.user.User;
import fib.br10.exception.reservation.ReservationCustomerUserIdNotMatchException;
import fib.br10.exception.reservation.ReservationNotFoundException;
import fib.br10.exception.reservation.ReservationSpecialistUserIdNotMatchException;
import fib.br10.mapper.ReservationMapper;
import fib.br10.repository.ReservationDetailRepository;
import fib.br10.repository.ReservationRepository;
import fib.br10.service.abstracts.NotificationService;
import fib.br10.service.abstracts.ReservationService;
import fib.br10.service.abstracts.SpecialistCustomerService;
import fib.br10.service.abstracts.WebSocketHandler;
import fib.br10.utility.Messages;
import fib.br10.utility.WebSocketQueues;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
public class ReservationServiceImpl implements ReservationService {

    ReservationRepository reservationRepository;
    ReservationMapper reservationMapper;
    SpecialistServiceManager specialistServiceManager;
    SpecialistCustomerService specialistCustomerService;
    WebSocketHandler webSocketHandler;
    UserService userService;
    RequestContextProvider provider;
    ReservationDetailRepository reservationDetailRepository;
    NotificationService notificationService;
    JPAQueryFactory jpa;

    @Transactional
    public ReservationResponse updateReservation(UpdateReservationRequest request) {
        Long userId = provider.getUserId();
        Integer reservationSource = request.getReservationSource();

        if (reservationSource.equals(ReservationSource.APP.getValue())
            && !userId.equals(request.getCustomerUserId())
        ) {
            throw new ReservationCustomerUserIdNotMatchException();
        }

        if (reservationSource.equals(ReservationSource.MANUAL.getValue())
            && !userId.equals(request.getSpecialistUserId())
        ) {
            throw new ReservationSpecialistUserIdNotMatchException();
        }

        Reservation reservation = findById(request.getReservationId());

        specialistCustomerService.checkIsCustomerBlocked(request.getSpecialistUserId(), request.getCustomerUserId());

        SpecialistService service = specialistServiceManager.findById(request.getSpecialistServiceId());

        OffsetDateTime start = request.getReservationDate();
        OffsetDateTime end = start.plusMinutes(service.getDuration());


        boolean reservationExist = reservationRepository.reservationExists(
                request.getSpecialistUserId(),
                ReservationStatus.PENDING.getValue(),
                start,
                end,
                request.getReservationId()
        );

        //add validation servicelerin hepsi varmi yoksa yokmu

        if (reservationExist) {
            //TODO: change it more readable error
            throw new BaseException(Messages.RESERVATION_CONFLICT);
        }

        reservation = reservationMapper.updateReservationRequestToReservation(reservation, request);

        reservationRepository.save(reservation);

        ReservationResponse response = prepareResponse(reservation,
                request.getSpecialistUserId(),
                request.getCustomerUserId()
        );

        webSocketHandler.publish(WebSocketQueues.RESERVATION_UPDATED, response, provider.getPhoneNumber());

        return response;
    }

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        //add check for availability

        boolean isSpecialist = ReservationSource.MANUAL.getValue().equals(request.getReservationSource());
        if (!isSpecialist && !ReservationSource.APP.getValue().equals(request.getReservationSource())
        ) {
            throw new BaseException("ReservationSource duz gonder 1 ya 2");
        }

        if (isSpecialist) {
            if (Objects.isNull(request.getCustomerUserId())) {
                throw new BaseException("Customer id null ola bilmez");
            }
            request.setSpecialistUserId(provider.getUserId());
        } else {
            if (Objects.isNull(request.getSpecialistServiceIds())) {
                throw new BaseException("Customer id null ola bilmez");
            }
            request.setCustomerUserId(provider.getUserId());
        }


        specialistCustomerService.checkIsCustomerBlocked(request.getSpecialistUserId(), request.getCustomerUserId());

        validateReservation(
                request.getReservationSource(),
                provider.getUserId(),
                request.getSpecialistUserId(),
                request.getCustomerUserId()
        );

        List<SpecialistService> services = specialistServiceManager.findAllByIds(request.getSpecialistServiceIds());
        BigDecimal totalPrice = BigDecimal.valueOf(0L);
        int totalDuration = 0;

        for (SpecialistService service : services) {
            //TODO: add this to validateReservation mehtod
            OffsetDateTime start = request.getReservationDate();
            OffsetDateTime end = start.plusMinutes(service.getDuration());

            boolean reservationExist = reservationRepository.reservationExists(
                    request.getSpecialistUserId(),
                    ReservationStatus.PENDING.getValue(),
                    start,
                    end
            );

            if (reservationExist) {
                //TODO: change it more readable error
                throw new BaseException("Bu tarixe reservasiya olunub");
            }
            totalPrice = totalPrice.add(service.getPrice());
            totalDuration = totalDuration + service.getDuration();

        }

        //TODO:Create new reservation
        Reservation newReservation = new Reservation();
        newReservation = reservationMapper.createReservationRequestToReservation(newReservation, request);
        newReservation.setPrice(totalPrice);
        newReservation.setReservationStatus(ReservationStatus.PENDING.getValue());
        newReservation.setDuration(totalDuration);
        newReservation = reservationRepository.save(newReservation);

        List<ReservationDetail> reservationDetails = new ArrayList<>();
        for (SpecialistService service : services) {
            reservationDetails.add(ReservationDetail.builder()
                    .reservationId(newReservation.getId())
                    .serviceId(service.getId())
                    .price(service.getPrice())
                    .duration(service.getDuration())
                    .build());
        }

        reservationDetailRepository.saveAll(reservationDetails);

        ReservationResponse response = prepareResponse(
                newReservation,
                request.getSpecialistUserId(),
                request.getCustomerUserId()
        );


        webSocketHandler.publish(WebSocketQueues.RESERVATION_CREATED, response, provider.getPhoneNumber());
        return response;
    }

    @Transactional
    public Long cancelReservation(CancelReservationRequest request) {
        QReservation res = QReservation.reservation;

        BooleanBuilder where = new BooleanBuilder();
        where.and(res.id.eq(request.getReservationId()));
        where.and(res.reservationStatus.eq(ReservationStatus.PENDING.getValue()));
        where.and(res.status.eq(EntityStatus.ACTIVE.getValue()));

        Reservation reservation = findOne(where);

        reservation.setReservationStatus(ReservationStatus.CANCELLED.getValue());
        reservationRepository.save(reservation);


        webSocketHandler.publish(WebSocketQueues.RESERVATION_CANCELED,
                new ReservationResponse(request.getReservationId()),
                provider.getPhoneNumber()
        );

        notificationService.send(
                PushNotificationRequest.builder()
                        .body("Reservation legv edildi")
                        .title("RESERVATION_CANCELED")
                        .build(),
                provider.getUserId()
        );

        //notifiation to customerUser

        return reservation.getId();
    }

    public List<ReservationResponse> findAllReservations() {
        OffsetDateTime now = DateUtil.getCurrentDateTime();

        OffsetDateTime startOfDay = now.truncatedTo(ChronoUnit.DAYS);
        OffsetDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        log.info("Start of day: {}", startOfDay);
        log.info("End of day: {}", endOfDay);

        List<ReservationResponse> reservations = reservationRepository.findAllPendingReservations(provider.getUserId(),
                startOfDay, endOfDay, EntityStatus.ACTIVE.getValue(), ReservationStatus.PENDING.getValue()
        );

        if (reservations.isEmpty()) {
            return reservations;
        }

        List<Long> reservationIds = reservations.stream()
                .map(ReservationResponse::getId)
                .collect(Collectors.toList());

        List<ReservationDetailResponse> reservationDetails = reservationDetailRepository.findAllReservationDetails(reservationIds);

        Map<Long, List<ReservationDetailResponse>> reservationDetailsMap = reservationDetails.stream()
                .collect(Collectors.groupingBy(ReservationDetailResponse::getReservationId));

        reservations.parallelStream().forEach(reservation -> {
            List<ReservationDetailResponse> details = reservationDetailsMap.get(reservation.getId());

            reservation.setReservationDetail(details);
        });

        return reservations;
    }

    public Reservation findOne(BooleanBuilder predicate) {
        return reservationRepository.findOne(predicate).orElseThrow(ReservationNotFoundException::new);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
    }

    @Override
    public List<ReservationResponse> findAllByCustomerId(Long customerId) {
        List<Reservation> reservation = reservationRepository.findAllByCustomerUserId(customerId);

        return reservation.stream()
                .map(res -> prepareResponse(res, res.getSpecialistUserId(), res.getCustomerUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerHistoryResponse> findHistory(Long customerId) {
        QReservation r = QReservation.reservation;
        QUser su = QUser.user;
        QReservationDetail rd = QReservationDetail.reservationDetail;
        QSpecialistService ss = QSpecialistService.specialistService;

        return jpa
                .select(Projections.constructor(
                        CustomerHistoryResponse.class,
                        r.id,
                        r.reservationDate,
                        r.reservationStatus,
                        su.username,
                        r.specialistUserId,
                        Expressions.stringTemplate("string_agg({0}, ',')", ss.name).coalesce(""),
                        r.price
                ))
                .from(r)
                .leftJoin(su).on(r.specialistUserId.eq(su.id))
                .leftJoin(rd).on(rd.reservationId.eq(r.id))
                .leftJoin(ss).on(ss.id.eq(rd.serviceId))
                .where(r.customerUserId.eq(customerId))
                .groupBy(r.id, r.reservationDate, r.reservationStatus, su.username, r.specialistUserId, r.price)
                .fetch();
    }

    @Override
    public List<CustomerHistoryDetailsDTO> getCustomerHistoryByReservation(long reservationId) {

        QReservationDetail reservationDetail = QReservationDetail.reservationDetail;
        QSpecialistService specialistService = QSpecialistService.specialistService;
        QUser user = QUser.user;
        QImage image = QImage.image;
// queryFactory, JPAQueryFactory nesnesidir; injection veya new ile oluşturabilirsiniz.
        List<CustomerHistoryDetailsDTO> result = jpa
                .from(reservationDetail)
                // ReservationDetail'deki serviceId ile SpecialistService arasında join:
                .leftJoin(specialistService).on(reservationDetail.serviceId.eq(specialistService.id))
                // SpecialistService'deki specialistUserId ile User arasında join:
                .leftJoin(user).on(specialistService.specialistUserId.eq(user.id))
                // GroupBy ile reservationId bazında gruplayıp, her rezervasyon için DTO oluşturuyoruz:
                .leftJoin(image).on(specialistService.imageId.eq(image.id))
                // GroupBy ile reservationId bazında gruplayıp, her rezervasyon için DTO oluşturuyoruz:
                .transform(GroupBy.groupBy(reservationDetail.reservationId).list(
                        Projections.constructor(CustomerHistoryDetailsDTO.class,
                                reservationDetail.reservationId,                          // reservationId
                                reservationDetail.createdDate,                            // Örneğin BaseEntity’den gelen tarih alanı (reservationDate)
                                reservationDetail.status,                      // rezervasyon durumu (reservationStatus)
                                user.name,                                                // specialistName (User tablosundan örnek olarak)
                                user.id,                                                  // specialistId
                                reservationDetail.price,                                  // rezervasyon fiyatı
                                // Servis listesini nested olarak maplemek için:
                                GroupBy.list(
                                        Projections.constructor(ServiceResponseDTO.class,
                                                specialistService.id,                             // serviceId
                                                specialistService.name,                           // serviceName
                                                specialistService.price,                          // price
                                                specialistService.duration,                       // duration
                                                specialistService.description,                    // description
                                                image.path                         // imageId
                                        )
                                )
                        )
                ));


        return result;
    }

    private ReservationResponse prepareResponse(Reservation reservation,
                                                Long specialistUserId,
                                                Long customerUserId) {

        User specialistUser = userService.findById(specialistUserId);
        User customerUser = userService.findById(customerUserId);

        ReservationResponse response = new ReservationResponse();
        response = reservationMapper.reservationToReservationResponse(response, reservation);
        response.setSpecialistUsername(specialistUser.getUsername());
        response.setCustomerUsername(customerUser.getUsername());
        response.setCustomerUserPhoneNumber(customerUser.getPhoneNumber());

        return response;
    }

    private void validateReservation(Integer source,
                                     Long userId,
                                     Long specialistUserId,
                                     Long cutomerUserId) {
        if (source.equals(ReservationSource.MANUAL.getValue()) && !userId.equals(specialistUserId)) {
            throw new ReservationSpecialistUserIdNotMatchException();
        }

        if (source.equals(ReservationSource.APP.getValue()) && !userId.equals(cutomerUserId)) {
            throw new ReservationCustomerUserIdNotMatchException();
        }

        userService.existsById(cutomerUserId);
        userService.existsByIdAndUserRoleSpecialist(specialistUserId);
    }

}