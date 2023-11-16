package com.example.subscribify.service.payment.discountstrategy;

public class NoDiscountPolicy implements DiscountPolicy {

    @Override
    public long calculateDiscountedAmount(long price, Double discountValue) {
        return price;
    }
}
