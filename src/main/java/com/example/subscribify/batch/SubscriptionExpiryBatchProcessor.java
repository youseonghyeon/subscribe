package com.example.subscribify.batch;

import com.example.subscribify.service.subscribe.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class SubscriptionExpiryBatchProcessor {

    private final SubscriptionService subscriptionService;
    private final int durationValue;
    private final TimeUnit timeUnit;

    /**
     * Scheduled task to be executed at midnight every day.
     * CRON breakdown:
     * - Seconds: 0 (At the beginning of the minute)
     * - Minutes: 0 (At the beginning of the hour)
     * - Hours: 0 (At midnight)
     * - Day of month: * (Every day of the month)
     * - Month: * (Every month)
     * - Day of week: * (Every day of the week)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void execute() {
        markAsExpired();
        deleteUnpaidSubscriptions();
    }

    private void deleteUnpaidSubscriptions() {
        long days = timeUnit.toDays(durationValue);
        LocalDateTime dateTime = LocalDateTime.now().minusDays(days);
        Integer deletedCount = subscriptionService.deleteUnpaidSubscriptions(dateTime);
        log.info("Processed {} deleted subscriptions.", deletedCount);
    }


    private void markAsExpired() {
        LocalDateTime now = LocalDateTime.now();
        Integer expiredCount = subscriptionService.expireSubscriptions(now);
        log.info("Processed {} expired subscriptions.", expiredCount);
    }
}
