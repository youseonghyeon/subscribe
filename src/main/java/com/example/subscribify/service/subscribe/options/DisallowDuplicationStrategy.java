package com.example.subscribify.service.subscribe.options;

import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.Subscription;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.entity.SubscriptionStatus;
import org.springframework.stereotype.Component;

@Component
public class DisallowDuplicationStrategy implements SubscriptionStrategy {
    @Override
    public Subscription apply(Customer customer, SubscriptionPlan subscriptionPlan) {

        // 이미 동일한 plan을 구독중이라면, 예외 처리
        if (customer.getSubscriptions().stream()
                .anyMatch(subscription ->
                        subscription.getSubscriptionPlan().equals(subscriptionPlan) &&
                                subscription.getStatus().equals(SubscriptionStatus.ACTIVE))) {
            throw new RuntimeException("Customer already has an active subscription.");
        }

        Subscription subscription = Subscription.builder()
                .subscribeName(subscriptionPlan.getPlanName())
                .durationMonth(subscriptionPlan.getDuration())
                .status(SubscriptionStatus.PENDING)
                .price(subscriptionPlan.getPrice())
                .discountRate(subscriptionPlan.getDiscount())
                .discountedPrice(subscriptionPlan.getDiscountedPrice())
                .customer(customer)
                .subscriptionPlan(subscriptionPlan)
                .build();
        if (subscriptionPlan.getPrice() <= 0) {
            subscription.activate();
        }
        return subscription;
    }
}
