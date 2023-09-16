package com.example.subscribify.controller;

import com.example.subscribify.dto.controller.PaymentRequest;
import com.example.subscribify.dto.controller.PaymentResponse;
import com.example.subscribify.entity.PaymentStatus;
import com.example.subscribify.service.payment.PgService;
import com.example.subscribify.service.subscribe.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PgService pgService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/payment/exec")
    public String execPayment(@RequestBody PaymentRequest paymentRequest) {
        // TODO 파라미터 검증 로직 추가

        PaymentResponse paymentResponse = pgService.processPayment(paymentRequest);
        if (PaymentStatus.COMPLETED.equals(paymentResponse.getStatus())) {
            subscriptionService.activateSubscribe(paymentRequest.getSubscriptionId());
        }

        return "redirect:/";
    }
}
