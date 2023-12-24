package com.example.subscribify.controller;

import com.example.subscribify.dto.controller.PaymentRequest;
import com.example.subscribify.entity.Payment;
import com.example.subscribify.service.payment.PaymentService;
import com.example.subscribify.service.payment.PgService;
import com.example.subscribify.service.subscribe.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("/history/{applicationId}")
    public String getPaymentHistory(@PathVariable Long applicationId, Model model, Pageable pageable) {
        Page<Payment> payments = paymentService.getPaymentLog(applicationId, pageable);
        model.addAttribute("payments", payments);
        return "application/payment-history";
    }
}
