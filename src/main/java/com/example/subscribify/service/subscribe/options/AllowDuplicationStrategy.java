package com.example.subscribify.service.subscribe.options;

import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.Subscription;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.entity.SubscriptionStatus;
import org.springframework.stereotype.Component;

@Component
public class AllowDuplicationStrategy implements SubscriptionStrategy {
    @Override
    public Subscription apply(Customer customer, SubscriptionPlan subscriptionPlan) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
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
        if (subscriptionPlan.getPrice() == 0) {
            subscription.activate();
        }
        return subscription;
    }

}
