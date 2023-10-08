package com.example.subscribify.service.customer;

import com.example.subscribify.entity.Customer;
import com.example.subscribify.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public Customer getOrCreateCustomer(String customerId, Long applicationId) {
        return customerRepository.findByCustomerIdAndApplicationId(customerId, applicationId)
                .orElseGet(() -> createAndSaveCustomer(customerId, applicationId));
    }

    private Customer createAndSaveCustomer(String customerId, Long applicationId) {
        Customer newCustomer = Customer.builder()
                .customerId(customerId)
                .applicationId(applicationId)
                .build();
        return customerRepository.save(newCustomer);
    }


    public List<Customer> getCustomersByApplicationId(Long applicationId) {
        return customerRepository.findByApplicationId(applicationId);
    }

    public Customer getCustomerByCustomerIdAndApplicationId(String customerId, Long applicationId) {
        return customerRepository.findByCustomerIdAndApplicationId(customerId, applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer ID"));
    }

    public Customer getCustomerByCustomerIdAndApplicationIdWithSubscriptions(String customerId, Long applicationId) {
        return customerRepository.findByCustomerIdAndApplicationId(customerId, applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer ID"));
    }
}
