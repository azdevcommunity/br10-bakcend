package fib.br10.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fib.br10.core.entity.EntityStatus;
import fib.br10.core.exception.BaseException;
import fib.br10.dto.specialist.specialistprofile.request.BlockCustomerRequest;
import fib.br10.dto.specialist.specialistprofile.request.UnBlockCustomerRequest;
import fib.br10.dto.specialist.specialistprofile.response.SpecialistBlockedCustomerResponse;
import fib.br10.entity.specialist.QSpecialistBlockedCustomer;
import fib.br10.entity.specialist.SpecialistBlockedCustomer;
import fib.br10.entity.user.QUser;
import fib.br10.exception.user.BlockedCustomerNotFoundException;
import fib.br10.exception.user.CustomerAlreadyBlockedException;
import fib.br10.exception.user.CustomerBlockedBySpecialistException;
import fib.br10.repository.SpecialistBlockedCustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static fib.br10.utility.CacheKeys.SPECIALIST_BLOCKED_CUSTOMERS;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecialistBlockedCustomerService {
    SpecialistBlockedCustomerRepository specialistBlockedCustomerRepository;
    JPAQueryFactory jpaQuery;

    @CacheEvict(value = SPECIALIST_BLOCKED_CUSTOMERS, key = "#userId")
    public boolean unblockCustomer(UnBlockCustomerRequest request, Long userId) {
        SpecialistBlockedCustomer blockedCustomer = findByCustomerUserIdAndSpecialistId(
                request.getCustomerUserId(),
                userId
        );

        specialistBlockedCustomerRepository.delete(blockedCustomer);

        return true;
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
    public boolean blockCustomer(BlockCustomerRequest request, Long userId) {
        checkIsCustomerBlocked(userId, request.getCustomerUserId(), new CustomerAlreadyBlockedException());

        SpecialistBlockedCustomer specialistBlockedCustomer = SpecialistBlockedCustomer.builder()
                .specialistUserId(userId)
                .customerUserId(request.getCustomerUserId())
                .reason(request.getReason())
                .build();

        specialistBlockedCustomerRepository.save(specialistBlockedCustomer);

        return true;
    }

    public SpecialistBlockedCustomer findByCustomerUserIdAndSpecialistId(Long customerUserId, Long specialistUserId) {
        return specialistBlockedCustomerRepository
                .findByCustomerUserIdAndSpecialistUserIdAndStatus(customerUserId, specialistUserId,
                        EntityStatus.ACTIVE.getValue())
                .orElseThrow(BlockedCustomerNotFoundException::new);
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

}
