package com.example.subscribify.config;

import com.example.subscribify.batch.SubscriptionExpiryBatchProcessor;
import com.example.subscribify.service.subscribe.SubscriptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class BatchConfig {

    @Value("${subscription.expiry.durationValue:7}")
    private int durationValue;

    @Bean
    public SubscriptionExpiryBatchProcessor subscriptionExpiryBatchProcessor(SubscriptionService subscriptionService) {
        return new SubscriptionExpiryBatchProcessor(subscriptionService, durationValue, TimeUnit.DAYS);
    }
}
