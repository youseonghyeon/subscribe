package com.example.subscribify.service.payment.discountstrategy;

public class FixedDiscountPolicy implements DiscountPolicy {
    @Override
    public long calculateDiscountedAmount(long price, Double discountValue) {
        if (discountValue == null) {
            return price;
        }
        double doubleValue = discountValue.doubleValue();
        long discountedPrice = price - (long) doubleValue;
        if (discountedPrice > price || discountedPrice < 0) {
            throw new IllegalArgumentException("Invalid discount value: Discounted price cannot be greater than original price or negative");
        }
        return discountedPrice;
    }
}
