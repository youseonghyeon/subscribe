package com.example.subscribify.service.payment.strategy;

import com.example.subscribify.entity.DiscountUnit;

public interface DiscountPolicy {
    long calculateDiscountAmount(long price, DiscountUnit discountUnit, double discountValue);
}
