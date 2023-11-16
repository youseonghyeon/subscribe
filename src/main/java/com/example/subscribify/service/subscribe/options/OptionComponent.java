package com.example.subscribify.service.subscribe.options;

import com.example.subscribify.dto.OptionResult;
import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.SubscriptionPlan;

public interface OptionComponent {
    OptionResult apply(Customer customer, SubscriptionPlan subscriptionPlan);
}
