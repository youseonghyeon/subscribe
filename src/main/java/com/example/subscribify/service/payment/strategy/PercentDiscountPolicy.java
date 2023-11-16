package com.example.subscribify.service.payment.strategy;

import com.example.subscribify.entity.DiscountUnit;

public class PercentDiscountPolicy implements DiscountPolicy {

    @Override
    public long calculateDiscountAmount(long price, DiscountUnit discountUnit, double discountValue) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
