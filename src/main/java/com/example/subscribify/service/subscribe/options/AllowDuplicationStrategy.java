package com.example.subscribify.service.subscribe.options;

import com.example.subscribify.dto.OptionResult;
import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.SubscriptionPlan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllowDuplicationStrategy extends OptionDecorator {

    public AllowDuplicationStrategy(OptionComponent optionComponent) {
        super(optionComponent);
    }

    @Override
    public OptionResult apply(Customer customer, SubscriptionPlan subscriptionPlan) {
        OptionResult optionResult = optionComponent.apply(customer, subscriptionPlan);

        // 부가 기능 위치
        log.info("AllowDuplicationStrategy.apply() called");

        return optionResult;
    }
}
