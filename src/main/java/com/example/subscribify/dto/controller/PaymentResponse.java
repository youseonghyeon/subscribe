package com.example.subscribify.dto.controller;

import com.example.subscribify.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {

    private String transactionId;

    private String userId;

    private String productId;

    private Long amount;

    private PaymentStatus status;
}
