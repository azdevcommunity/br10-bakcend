package fib.br10.service;

import com.querydsl.core.BooleanBuilder;
import fib.br10.core.dto.RequestById;
import fib.br10.core.entity.EntityStatus;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.DateUtil;
import fib.br10.dto.reservation.request.CancelReservationRequest;
import fib.br10.dto.reservation.request.CreateReservationRequest;
import fib.br10.dto.reservation.request.UpdateReservationRequest;
import fib.br10.dto.reservation.response.ReservationResponse;
import fib.br10.entity.reservation.QReservation;
import fib.br10.entity.reservation.Reservation;
import fib.br10.entity.reservation.ReservationSource;
import fib.br10.entity.reservation.ReservationStatus;
import fib.br10.entity.specialist.SpecialistService;
import fib.br10.entity.user.User;
import fib.br10.exception.reservation.ReservationCustomerUserIdNotMatchException;
import fib.br10.exception.reservation.ReservationNotFoundException;
import fib.br10.exception.reservation.ReservationSpecialistUserIdNotMatchException;
import fib.br10.mapper.ReservationMapper;
import fib.br10.repository.ReservationRepository;
import fib.br10.utility.Messages;
import fib.br10.utility.WebSocketQueues;
import fib.br10.service.abstracts.WebSocketHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;


@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Log4j2
public class ReservationService {

    ReservationRepository reservationRepository;
    ReservationMapper reservationMapper;
    SpecialistServiceManager specialistServiceManager;
    SpecialistBlockedCustomerService specialistBlockedCustomerService;
    WebSocketHandler webSocketHandler;
    UserService userService;
    RequestContextProvider provider;

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

        specialistBlockedCustomerService.checkIsCustomerBlocked(request.getSpecialistUserId(), request.getCustomerUserId());

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
        //bu userin baska specialist ucun olsa bele toqqusan reservi varmi
        boolean isSpecialist = ReservationSource.MANUAL.getValue().equals(request.getReservationSource());
        if (!isSpecialist && !ReservationSource.APP.getValue().equals(request.getReservationSource())
        ) {
            throw new BaseException("ReservationSource duz gonder 1 ya 2");
        }

        if (isSpecialist) {
            if (Objects.isNull(request.getCustomerUserId()))
                throw new BaseException("Customer id null ola bilmez");
            request.setSpecialistUserId(provider.getUserId());
        }

        if (!isSpecialist) {
            if (Objects.isNull(request.getSpecialistServiceId())) {
                throw new BaseException("Customer id null ola bilmez");
            }
            request.setCustomerUserId(provider.getUserId());
        }


        validateReservation(
                request.getReservationSource(),
                provider.getUserId(),
                request.getSpecialistUserId(),
                request.getCustomerUserId()
        );

        SpecialistService service = specialistServiceManager.findById(request.getSpecialistServiceId());

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

        //TODO:if client have reservatin for same time

        //Create reservation entity
        Reservation newReservation = new Reservation();
        newReservation = reservationMapper.createReservationRequestToReservation(newReservation, request);
        newReservation.setPrice(service.getPrice());
        newReservation.setReservationStatus(ReservationStatus.PENDING.getValue());
        newReservation.setDuration(service.getDuration());
        newReservation = reservationRepository.save(newReservation);

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

        //notifiation to customerUser

        return reservation.getId();
    }

    public List<ReservationResponse> findAllReservations(RequestById request) {
//        Long userId = ThreadContextUtil.get(ThreadContextConstants.CURRENT_USER_ID, Long.class);

        //check does user is specialist or not
        //TODO: in future it will be globall role based permision filter

//        User user = userService.findById(userId);
//        if (!user.getUserType().equals(UserType.SPECIALIST.getValue())) {
//            //TODO: CHANGE ERROR MESSAGE
//            throw new BaseException(Messages.ERROR);
//        }

        OffsetDateTime now = DateUtil.getCurrentDateTime();

        OffsetDateTime startOfDay = now.truncatedTo(ChronoUnit.DAYS);
        OffsetDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        log.info("Start of day: {}", startOfDay);
        log.info("End of day: {}", endOfDay);

        return reservationRepository.findAllPendingReservations(request.getId(),
                startOfDay, endOfDay, EntityStatus.ACTIVE.getValue(), ReservationStatus.PENDING.getValue()
        );
    }

    public Reservation findOne(BooleanBuilder predicate) {
        return reservationRepository.findOne(predicate).orElseThrow(ReservationNotFoundException::new);
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
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
        if (source.equals(ReservationSource.MANUAL.getValue())
            && !userId.equals(specialistUserId)
        ) {
            throw new ReservationSpecialistUserIdNotMatchException();
        }

        if (source.equals(ReservationSource.APP.getValue())
            && !userId.equals(cutomerUserId)
        ) {
            throw new ReservationCustomerUserIdNotMatchException();
        }

        userService.existsById(cutomerUserId);
        userService.existsByIdAndUserRoleSpecialist(specialistUserId);
    }
}
