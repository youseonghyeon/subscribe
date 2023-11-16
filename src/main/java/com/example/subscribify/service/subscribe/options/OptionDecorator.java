package com.example.subscribify.service.subscribe.options;

import com.example.subscribify.dto.OptionResult;
import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.SubscriptionPlan;

public class OptionDecorator implements OptionComponent {

    final OptionComponent optionComponent;

    public OptionDecorator(OptionComponent optionComponent) {
        this.optionComponent = optionComponent;
    }

    @Override
    public OptionResult apply(Customer customer, SubscriptionPlan subscriptionPlan) {
        return optionComponent.apply(customer, subscriptionPlan);
    }
}
