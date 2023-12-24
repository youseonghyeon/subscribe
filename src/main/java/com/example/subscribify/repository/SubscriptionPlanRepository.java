package com.example.subscribify.repository;

import com.example.subscribify.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    List<SubscriptionPlan> findAllByApplicationId(Long applicationId);
}
