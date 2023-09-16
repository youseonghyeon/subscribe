package com.example.subscribify.dto.controller;

import com.example.subscribify.entity.DiscountUnit;
import com.example.subscribify.entity.DurationUnit;
import com.example.subscribify.entity.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateSubscribeDto {

    private Long id;
    private String subscribeName;
    private Integer duration;
    private DurationUnit durationUnit; // Month, Year
    private Long price;
    private Double discount;
    private DiscountUnit discountType; // Percent, Fixed
    private Long discountedPrice;

    public UpdateSubscribeDto(SubscriptionPlan subscriptionPlan) {
        this.id = subscriptionPlan.getId();
        this.subscribeName = subscriptionPlan.getPlanName();
        this.duration = subscriptionPlan.getDuration();
        this.durationUnit = subscriptionPlan.getDurationUnit();
        this.price = subscriptionPlan.getPrice();
        this.discount = subscriptionPlan.getDiscount();
        this.discountType = subscriptionPlan.getDiscountType();
        this.discountedPrice = subscriptionPlan.getDiscountedPrice();
    }
}
