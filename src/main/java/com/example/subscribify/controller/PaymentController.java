package com.example.subscribify.controller;

import com.example.subscribify.dto.PaymentRequest;
import com.example.subscribify.dto.PaymentResponse;
import com.example.subscribify.entity.PaymentStatus;
import com.example.subscribify.service.payment.PgService;
import com.example.subscribify.service.subscribe.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PgService pgService;
    private final SubscribeService subscribeService;

    @PostMapping("/payment/exec")
    public String execPayment(@RequestBody PaymentRequest paymentRequest) {
        // TODO 파라미터 검증 로직 추가

        PaymentResponse paymentResponse = pgService.processPayment(paymentRequest);
        if (PaymentStatus.COMPLETED.equals(paymentResponse.getStatus())) {
            subscribeService.activateSubscribe(paymentRequest.getSubscriptionId());
        }

        return "redirect:/";
    }
}
