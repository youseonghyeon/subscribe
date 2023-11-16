package com.example.subscribify.service.payment.strategy;

import com.example.subscribify.entity.DiscountUnit;

public class DiscountPolicyFactory {
    public static DiscountPolicy create(DiscountUnit discountType) {
        return switch (discountType) {
            case PERCENT -> new PercentDiscountPolicy();
            case FIXED -> new FixedDiscountPolicy();
            case NONE -> new NoDiscountPolicy();
        };
    }
}
