package com.example.subscribify.dto;

import com.example.subscribify.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentDto {

    private String transactionId;

    private String userId;

    private String productId;

    private Long amount;

    private PaymentStatus status;
}
