package com.example.subscribify.repository;

import com.example.subscribify.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByProductId(Long subscriptionPlanId);
}
