package com.example.subscribify.dto.api;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EnrollSubscriptionRequest {

    private String customerId;
    private Long planId;

}
