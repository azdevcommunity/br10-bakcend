package fib.br10.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fib.br10.core.entity.EntityStatus;
import fib.br10.core.exception.BaseException;
import fib.br10.core.service.RequestContextProvider;
import fib.br10.core.utility.DateUtil;
import fib.br10.dto.customer.response.ReadReservationsResponse;
import fib.br10.dto.specialist.specialistprofile.request.BlockCustomerRequest;
import fib.br10.dto.specialist.specialistprofile.request.UnBlockCustomerRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistBlockedCustomerResponse;
import fib.br10.entity.reservation.QReservation;
import fib.br10.entity.reservation.QReservationDetail;
import fib.br10.entity.specialist.QSpecialistBlockedCustomer;
import fib.br10.entity.specialist.QSpecialistService;
import fib.br10.entity.specialist.SpecialistBlockedCustomer;
import fib.br10.entity.user.QUser;
import fib.br10.exception.user.BlockedCustomerNotFoundException;
import fib.br10.exception.user.CustomerAlreadyBlockedException;
import fib.br10.exception.user.CustomerBlockedBySpecialistException;
import fib.br10.repository.SpecialistBlockedCustomerRepository;
import fib.br10.service.abstracts.SpecialistCustomerService;
import fib.br10.utility.PaginationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static fib.br10.utility.CacheKeys.SPECIALIST_BLOCKED_CUSTOMERS;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecialistCustomerServiceImpl implements SpecialistCustomerService {
    SpecialistBlockedCustomerRepository specialistBlockedCustomerRepository;
    JPAQueryFactory jpaQuery;
    RequestContextProvider provider;

    @CacheEvict(value = SPECIALIST_BLOCKED_CUSTOMERS, key = "#userId")
    public SpecialistBlockedCustomerResponse unblockCustomer(UnBlockCustomerRequest request, Long userId) {
        SpecialistBlockedCustomer blockedCustomer = findByCustomerUserIdAndSpecialistId(
                request.getCustomerUserId(),
                userId
        );

        specialistBlockedCustomerRepository.delete(blockedCustomer);

        return getSpecialistBlockedCustomerResponse(userId, blockedCustomer);
    }

    @Cacheable(value = SPECIALIST_BLOCKED_CUSTOMERS, key = "#userId")
    public List<SpecialistBlockedCustomerResponse> getBlockedCustomers(Long userId) {
        QSpecialistBlockedCustomer spc = QSpecialistBlockedCustomer.specialistBlockedCustomer;
        QUser user = QUser.user;

        List<SpecialistBlockedCustomerResponse> users = jpaQuery.select(Projections.constructor(
                        SpecialistBlockedCustomerResponse.class,
                        user.id, user.username, user.phoneNumber, spc.createdDate))
                .from(spc)
                .innerJoin(user).on(user.id.eq(spc.customerUserId))
                .where(spc.specialistUserId.eq(userId)
                        .and(spc.status.eq(EntityStatus.ACTIVE.getValue())))
                .fetch();

        return new ArrayList<>(users);
    }

    @CacheEvict(value = SPECIALIST_BLOCKED_CUSTOMERS, key = "#userId")
    public SpecialistBlockedCustomerResponse blockCustomer(BlockCustomerRequest request, Long userId) {
        checkIsCustomerBlocked(userId, request.getCustomerUserId(), new CustomerAlreadyBlockedException());

        SpecialistBlockedCustomer specialistBlockedCustomer = SpecialistBlockedCustomer.builder()
                .specialistUserId(userId)
                .customerUserId(request.getCustomerUserId())
                .reason(request.getReason())
                .build();

        specialistBlockedCustomer = specialistBlockedCustomerRepository.save(specialistBlockedCustomer);

        return getSpecialistBlockedCustomerResponse(userId, specialistBlockedCustomer);
    }

    public SpecialistBlockedCustomer findByCustomerUserIdAndSpecialistId(Long customerUserId, Long specialistUserId) {
        return specialistBlockedCustomerRepository
                .findByCustomerUserIdAndSpecialistUserIdAndStatus(customerUserId, specialistUserId,
                        EntityStatus.ACTIVE.getValue())
                .orElseThrow(BlockedCustomerNotFoundException::new);
    }


    public List<ReadReservationsResponse> findAllReservations(Long pageSize, Long pageNumber, LocalDateTime reservationDate) {
        QUser user = QUser.user;
        QReservation r = QReservation.reservation;
        QReservationDetail rd = QReservationDetail.reservationDetail;
        QSpecialistService ss = QSpecialistService.specialistService;


        OffsetDateTime startOfDay = DateUtil.toOffsetDateTime(reservationDate.toLocalDate().atStartOfDay());
        OffsetDateTime endOfDay = DateUtil.toOffsetDateTime(reservationDate
                .toLocalDate()
                .atStartOfDay()
                .plusDays(1)
                .minusSeconds(1));

        List<ReadReservationsResponse> reservations = jpaQuery.select(
                        Projections.constructor(ReadReservationsResponse.class,
                                r.id,
                                rd.id,
                                r.reservationDate,
                                r.specialistUserId,
                                user.username,
                                r.customerUserId,
                                r.price,
                                r.reservationSource,
                                r.reservationStatus,
                                r.duration))
                .from(r)
                .leftJoin(user).on(user.id.eq(r.specialistUserId))
                .leftJoin(rd).on(rd.id.eq(r.id))
                .where(r.customerUserId.eq(provider.getUserId())
                        .and(r.reservationDate.between(startOfDay, endOfDay)))
                .limit(PaginationUtil.getPageSize(pageSize))
                .offset(PaginationUtil.getOffset(pageSize, pageNumber))
                .orderBy(r.reservationDate.desc())
                .fetch();

        List<Long> reservationDetailIds = reservations.stream()
                .map(ReadReservationsResponse::getDetailId)
                .toList();

        Map<Long, List<ReadReservationsResponse>> servicesByDetailId = jpaQuery.select(
                        Projections.constructor(ReadReservationsResponse.class, rd.id, ss.id, ss.name))
                .from(rd)
                .join(ss).on(rd.serviceId.eq(ss.id))
                .where(rd.id.in(reservationDetailIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(ReadReservationsResponse::getDetailId));

        reservations.forEach(reservation -> reservation.setServices(
                servicesByDetailId.getOrDefault(reservation.getDetailId(), Collections.emptyList()))
        );

        return reservations;
    }

    public void checkIsCustomerBlocked(Long specialistUserId, Long customerUserId) {
        checkIsCustomerBlocked(specialistUserId, customerUserId, new CustomerBlockedBySpecialistException());
    }

    private void checkIsCustomerBlocked(Long specialistUserId, Long customerUserId, BaseException exception) {
        boolean exists = specialistBlockedCustomerRepository.existsBySpecialistUserIdAndCustomerUserIdAndStatus(
                specialistUserId, customerUserId, EntityStatus.ACTIVE.getValue()
        );

        if (exists) {
            throw exception;
        }
    }

    private SpecialistBlockedCustomerResponse getSpecialistBlockedCustomerResponse(Long userId, SpecialistBlockedCustomer blockedCustomer) {
        QSpecialistBlockedCustomer spc = QSpecialistBlockedCustomer.specialistBlockedCustomer;
        QUser user = QUser.user;

        return jpaQuery.select(Projections.constructor(
                        SpecialistBlockedCustomerResponse.class,
                        user.id, user.username, user.phoneNumber, spc.createdDate))
                .from(spc)
                .innerJoin(user).on(user.id.eq(spc.customerUserId))
                .where(spc.specialistUserId.eq(userId)
                        .and(spc.customerUserId.eq(blockedCustomer.getCustomerUserId()))
                        .and(spc.status.eq(EntityStatus.ACTIVE.getValue())))
                .fetchFirst();
    }
}
