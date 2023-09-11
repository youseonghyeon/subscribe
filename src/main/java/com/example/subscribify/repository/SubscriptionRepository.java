package com.example.subscribify.repository;

import com.example.subscribify.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllBySubscriptionPlanId(Long subscriptionPlanId);
}
