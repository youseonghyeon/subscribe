package com.example.subscribify.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EnrollSubscriptionRequest {

    private String customerId;
    private Long planId;

}
