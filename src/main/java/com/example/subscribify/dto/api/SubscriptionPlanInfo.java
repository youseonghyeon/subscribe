package com.example.subscribify.dto.api;

import com.example.subscribify.entity.DiscountUnit;
import com.example.subscribify.entity.DurationUnit;
import com.example.subscribify.entity.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscriptionPlanInfo {
    private final Long id;
    private final String planName;
    private final Integer duration;
    private final DurationUnit durationUnit;
    private final Long price;
    private final Double discount;
    private final DiscountUnit discountType;
    private final Long discountedPrice;

    public SubscriptionPlanInfo(SubscriptionPlan subscriptionPlan) {
        this.id = subscriptionPlan.getId();
        this.planName = subscriptionPlan.getPlanName();
        this.duration = subscriptionPlan.getDuration();
        this.durationUnit = subscriptionPlan.getDurationUnit();
        this.price = subscriptionPlan.getPrice();
        this.discount = subscriptionPlan.getDiscount();
        this.discountType = subscriptionPlan.getDiscountType();
        this.discountedPrice = subscriptionPlan.getDiscountedPrice();
    }
}
