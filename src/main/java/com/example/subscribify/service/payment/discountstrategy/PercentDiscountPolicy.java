package com.example.subscribify.service.payment.discountstrategy;

public class PercentDiscountPolicy implements DiscountPolicy {

    @Override
    public long calculateDiscountedAmount(long price, Double discountValue) {
        if (discountValue == null) {
            return price;
        }

        long discountedPrice = (long) (price * (100 - discountValue) / 100);
        if (discountedPrice > price || discountedPrice < 0) {
            throw new IllegalArgumentException("Invalid discount value: Discounted price cannot be greater than original price or negative");
        }
        return discountedPrice;

    }
}
