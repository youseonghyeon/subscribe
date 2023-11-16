package com.example.subscribify.service.subscribe.options;

import com.example.subscribify.dto.OptionResult;
import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.SubscriptionPlan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteOptionComponent implements OptionComponent {
    @Override
    public OptionResult apply(Customer customer, SubscriptionPlan subscriptionPlan) {
        log.info("ConcreteOptionComponent.apply() called");
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (subscriptionPlan == null) {
            throw new IllegalArgumentException("SubscriptionPlan cannot be null");
        }
        return new OptionResult();
    }
}
