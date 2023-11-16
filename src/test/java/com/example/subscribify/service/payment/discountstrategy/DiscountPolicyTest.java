package com.example.subscribify.service.payment.discountstrategy;

import com.example.subscribify.entity.DiscountUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountPolicyTest {

    final long PRICE = 10000L;

    @Test
    void fixedDiscountPolicyTest() {
        DiscountPolicy fixedDiscountPolicy = DiscountPolicyFactory.createPolicy(DiscountUnit.FIXED);
        long fixValue = fixedDiscountPolicy.calculateDiscountedAmount(PRICE, 1000D);
        assertEquals(9000L, fixValue);
    }

    @Test
    void fixedDiscountPolicyWithNullDiscountTest() {
        DiscountPolicy fixedDiscountPolicy = DiscountPolicyFactory.createPolicy(DiscountUnit.FIXED);
        long fixValue = fixedDiscountPolicy.calculateDiscountedAmount(PRICE, null);
        assertEquals(PRICE, fixValue);
    }

    @Test
    void percentDiscountPolicyTest() {
        DiscountPolicy percentDiscountPolicyTest = DiscountPolicyFactory.createPolicy(DiscountUnit.PERCENT);
        long percentValue = percentDiscountPolicyTest.calculateDiscountedAmount(PRICE, 15.5);
        assertEquals(8450L, percentValue);
    }

    @Test
    void percentDiscountPolicyWithNullDiscountTest() {
        DiscountPolicy percentDiscountPolicyTest = DiscountPolicyFactory.createPolicy(DiscountUnit.PERCENT);
        long percentValue = percentDiscountPolicyTest.calculateDiscountedAmount(PRICE, null);
        assertEquals(PRICE, percentValue);
    }

    @Test
    void noDiscountPolicyTest() {
        DiscountPolicy noDiscountPolicy = DiscountPolicyFactory.createPolicy(DiscountUnit.NONE);
        long value = noDiscountPolicy.calculateDiscountedAmount(PRICE, 1000D);
        assertEquals(10000L, value);
    }

    @Test
    void noDiscountPolicyWithNullDiscountTest() {
        DiscountPolicy noDiscountPolicy = DiscountPolicyFactory.createPolicy(DiscountUnit.NONE);
        long value = noDiscountPolicy.calculateDiscountedAmount(10000L, null);
        assertEquals(PRICE, value);
    }
}
