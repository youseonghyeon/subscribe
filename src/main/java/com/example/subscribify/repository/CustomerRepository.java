package com.example.subscribify.repository;

import com.example.subscribify.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerId(String clientId);

    List<Customer> findByApplicationId(Long applicationId);

    Optional<Customer> findByCustomerIdAndApplicationId(String customerId, Long applicationId);
}
