package com.example.subscribify.service.payment.discountstrategy;


import org.springframework.lang.Nullable;

public interface DiscountPolicy {

    long calculateDiscountedAmount(long price, @Nullable Double discountValue);
}
