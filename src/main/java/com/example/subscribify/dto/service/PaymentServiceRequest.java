package com.example.subscribify.dto.service;

import com.example.subscribify.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentServiceRequest {

    private String transactionId;

    private String userId;

    private String productId;

    private Long amount;

    private PaymentStatus status;
}
