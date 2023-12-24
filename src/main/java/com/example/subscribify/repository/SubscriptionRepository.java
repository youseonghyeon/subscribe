package com.example.subscribify.repository;

import com.example.subscribify.entity.Subscription;
import com.example.subscribify.entity.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllBySubscriptionPlanId(Long subscriptionPlanId);

    @Query("SELECT s FROM Subscription s JOIN FETCH s.customer c WHERE s.subscriptionPlan.id = :planId")
    List<Subscription> findAllWithCustomerBySubscriptionPlanId(Long planId);

    List<Subscription> findAllByStatusAndEndDateBefore(SubscriptionStatus status, LocalDateTime endDate);
    List<Subscription> findAllByStatusAndEndDateBetween(SubscriptionStatus status, LocalDateTime startDate, LocalDateTime endDate);

    List<Subscription> findAllByStatusAndCreatedAtBefore(SubscriptionStatus subscriptionStatus, LocalDateTime localDate);


    @Query("SELECT s FROM Subscription s " +
            "JOIN s.subscriptionPlan p " +
            "JOIN p.application a " +
            "WHERE a.id = :applicationId AND s.status LIKE 'ACTIVE%' AND s.endDate >= :today")
    List<Subscription> findActiveSubscriptionsCountByApplicationId(Long applicationId, LocalDateTime today);

}
