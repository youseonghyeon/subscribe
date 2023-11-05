package com.example.subscribify.exception;

import java.util.NoSuchElementException;

public class SubscriptionPlanNotFoundException extends NoSuchElementException {

    public SubscriptionPlanNotFoundException(Long planId) {
        super("Invalid subscription plan ID: " + planId);
    }
}
