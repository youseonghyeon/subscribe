package com.example.subscribify.batch;

import com.example.subscribify.service.subscribe.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class SubscriptionExpiryBatchProcessor {
    private final SubscriptionService subscriptionService;

    @Scheduled(cron = "0 0 0 * * *")
    public void execute() {
        LocalDateTime now = LocalDateTime.now();
        Integer expiredCount = subscriptionService.expireSubscriptions(now);
        log.info("Processed {} expired subscriptions.", expiredCount);
    }
}
