package com.example.subscribify.controller;

import com.example.subscribify.dto.controller.PaymentRequest;
import com.example.subscribify.dto.controller.PaymentResponse;
import com.example.subscribify.entity.Payment;
import com.example.subscribify.entity.PaymentStatus;
import com.example.subscribify.service.payment.PaymentService;
import com.example.subscribify.service.payment.PgService;
import com.example.subscribify.service.subscribe.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PgService pgService;
    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;

    @PostMapping("/payment/exec")
    public String execPayment(@RequestBody PaymentRequest paymentRequest) {
        // TODO 파라미터 검증 로직 추가

//        PaymentResponse paymentResponse = pgService.processPayment(paymentRequest);
//        if (PaymentStatus.COMPLETED.equals(paymentResponse.getStatus())) {
//            subscriptionService.activateSubscribe(paymentRequest.getSubscriptionId());
//        }

        return "redirect:/";
    }

    @GetMapping("/history/{subscriptionPlanId}")
    public String getPaymentHistory(@PathVariable Long subscriptionPlanId) {
        List<Payment> payments = paymentService.getPaymentLog(subscriptionPlanId);
        return "application/payment-history";
    }
}
