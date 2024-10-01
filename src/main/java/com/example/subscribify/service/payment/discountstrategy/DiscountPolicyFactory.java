package com.example.subscribify.service.payment.discountstrategy;

import com.example.subscribify.entity.DiscountUnit;

public class DiscountPolicyFactory {
    public static DiscountPolicy createPolicy(DiscountUnit discountType) {
        return switch (discountType) {
            case PERCENT -> new PercentDiscountPolicy();
            case FIXED -> new FixedDiscountPolicy();
            case NONE -> new NoDiscountPolicy();
            default -> throw new IllegalArgumentException("Invalid discount type");
        };
    }
}
