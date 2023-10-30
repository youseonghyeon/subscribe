package com.example.subscribify.repository;

import com.example.subscribify.entity.Subscription;
import com.example.subscribify.entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllBySubscriptionPlanId(Long subscriptionPlanId);

    @Query("select s from Subscription s join fetch s.customer c where s.subscriptionPlan.id = :planId")
    List<Subscription> findAllWithCustomerBySubscriptionPlanId(Long planId);

    List<Subscription> findAllByStatusAndEndDateBefore(SubscriptionStatus status, LocalDateTime endDate);
    List<Subscription> findAllByStatusAndEndDateBetween(SubscriptionStatus status, LocalDateTime startDate, LocalDateTime endDate);

    List<Subscription> findAllByStatusAndCreatedAtBefore(SubscriptionStatus subscriptionStatus, LocalDateTime localDate);
}
