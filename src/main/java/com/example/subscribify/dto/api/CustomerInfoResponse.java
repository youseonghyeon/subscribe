package com.example.subscribify.dto.api;

import com.example.subscribify.entity.Customer;
import lombok.Data;

@Data
public class CustomerInfoResponse {
    private final Long id;
    private final String customerId;
    private final Long applicationId;

    public CustomerInfoResponse(Customer customer) {
        this.id = customer.getId();
        this.customerId = customer.getCustomerId();
        this.applicationId = customer.getApplicationId();
    }
}
