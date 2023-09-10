package com.example.subscribify.repository;

import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    List<SubscriptionPlan> findByUser(User user);

}
