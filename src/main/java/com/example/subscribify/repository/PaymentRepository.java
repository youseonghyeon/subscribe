package com.example.subscribify.repository;

import com.example.subscribify.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findAllByApplicationId(Long applicationId, Pageable pageable);

    @Query("select sum(p.amount) from Payment p where p.applicationId = :applicationId")
    Long sumAmountByApplicationId(Long applicationId);
}
