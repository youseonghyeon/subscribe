package com.example.subscribify.dto.service;

import com.example.subscribify.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnrollSubscriptionServiceRequest {
    Customer customer;
    Long planId;
    String apiKey;
    String secretKey;
}
