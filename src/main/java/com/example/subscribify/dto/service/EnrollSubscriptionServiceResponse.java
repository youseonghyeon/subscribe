package com.example.subscribify.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnrollSubscriptionServiceResponse {

    private String errorMessage;
    private Long subscriptionId;


    public EnrollSubscriptionServiceResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public EnrollSubscriptionServiceResponse(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
