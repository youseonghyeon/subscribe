package com.example.subscribify.service.subscribe.options;

import com.example.subscribify.dto.OptionResult;
import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.entity.SubscriptionStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DisallowDuplicationStrategy extends OptionDecorator {

    public DisallowDuplicationStrategy(OptionComponent optionComponent) {
        super(optionComponent);
    }

    /**
     * 주의 :: customer 객체에 연결된 subscription 객체들은 eagerLoading 이 필요함.
     * TODO (LazyLoading 이 가능 하도록 수정 하거나 해당 메서드를 위해 EagerLoading 을 수행 하도록 변경 해야 함)
     */
    @Override
    public OptionResult apply(Customer customer, SubscriptionPlan subscriptionPlan) {
        OptionResult optionResult = optionComponent.apply(customer, subscriptionPlan);
        if (!customer.getSubscriptions().isEmpty() && hasActiveSubscriptionForPlan(customer, subscriptionPlan)) {
            throw new RuntimeException("Customer already has an active subscription.");
        }
        // 부가 기능 위치

        log.info("DisallowDuplicationStrategy.apply() called");

        return optionResult;
    }

    private static boolean hasActiveSubscriptionForPlan(Customer customer, SubscriptionPlan subscriptionPlan) {
        return customer.getSubscriptions().stream()
                .anyMatch(subscription ->
                        subscription.getSubscriptionPlan().equals(subscriptionPlan) &&
                                subscription.getStatus().equals(SubscriptionStatus.ACTIVE));
    }
}
