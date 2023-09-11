package com.example.subscribify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActivateSubscriptionRequest {

    private Long subscriptionId;
    private String apiKey;
    private String secretKey;
}
