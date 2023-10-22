package com.example.subscribify.repository;

import com.example.subscribify.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByApplicationId(Long applicationId);

    @Query("select sum(p.amount) from Payment p where p.applicationId = :applicationId")
    Long sumAmountByApplicationId(Long applicationId);
}
