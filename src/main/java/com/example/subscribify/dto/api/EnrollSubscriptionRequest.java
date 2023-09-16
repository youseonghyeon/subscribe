package com.example.subscribify.dto.api;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EnrollSubscriptionRequest {

    private String customerId;
    private Long applicationId;
    private Long planId;
    private String apiKey;
    private String secretKey;

}
