package com.example.subscribify.batch;

import com.example.subscribify.entity.Subscription;
import com.example.subscribify.service.payment.PaymentService;
import com.example.subscribify.service.subscribe.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class SubscriptionRenewalBatch {

    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;

    @Scheduled(cron = "0 0 0 * * *")
    public void execute() {
    }

    public void renewSubscription() {
        LocalDate today = LocalDate.now();
        List<Subscription> subscriptions = subscriptionService.getReachedExpireDaySubscriptions(today);
        subscriptions.forEach(subscription -> {
            // 결제
//            paymentService.pay(subscription);
            // 연장
//            subscriptionService.renewSubscription(subscription);

        });
    }

}
