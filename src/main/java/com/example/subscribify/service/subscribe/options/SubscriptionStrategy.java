package com.example.subscribify.service.subscribe.options;

import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.Subscription;
import com.example.subscribify.entity.SubscriptionPlan;

public interface SubscriptionStrategy {

    Subscription apply(Customer customer, SubscriptionPlan subscriptionPlan);



}
