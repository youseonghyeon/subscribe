package com.example.subscribify.dto;

import com.example.subscribify.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest {

    private Long subscriptionId;

    private String transactionId;

    private String userId;

    private String productId;

    private Long amount;

    private PaymentStatus status;
}
