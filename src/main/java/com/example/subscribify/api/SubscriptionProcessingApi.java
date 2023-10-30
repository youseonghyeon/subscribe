package com.example.subscribify.api;

import com.example.subscribify.domain.AuthApplication;
import com.example.subscribify.dto.api.ActivateSubscriptionRequest;
import com.example.subscribify.dto.api.EnrollSubscriptionRequest;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.Application;
import com.example.subscribify.entity.PaymentStatus;
import com.example.subscribify.entity.Subscription;
import com.example.subscribify.repository.SubscriptionRepository;
import com.example.subscribify.service.payment.PaymentService;
import com.example.subscribify.service.subscribe.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubscriptionProcessingApi {

    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;
    private final SubscriptionRepository subscriptionRepository;

    /**
     * 구독 등록 api
     *
     * @param request
     * @param application
     * @return
     */
    @PostMapping("/enroll")
    public ResponseEntity<EnrollSubscriptionServiceResponse> enrollSubscription(
            @RequestBody EnrollSubscriptionRequest request,
            @AuthApplication Application application) {

        EnrollSubscriptionServiceResponse serviceResponse = subscriptionService.enrollSubscribe(request.getCustomerId(), request.getPlanId(), application.getApiKey());

        if (serviceResponse.hasError()) {
            return ResponseEntity.badRequest().body(serviceResponse);
        }

        return ResponseEntity.ok(serviceResponse);
    }

//    /**
//     * 구독 활성화 api
//     *
//     * @param request
//     * @param application
//     * @return
//     */
//    @PostMapping("/activate-pay")
//    public ResponseEntity<Void> activateSubscription(
//            @RequestBody ActivateSubscriptionRequest request,
//            @AuthApplication Application application) {
//
//        subscriptionService.activateSubscribe(request.getSubscriptionId(), application.getApiKey());
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activateSubscriptionAndPayment(
            @RequestBody ActivateSubscriptionRequest request,
            @AuthApplication Application application) {

        subscriptionService.activateSubscribe(request.getSubscriptionId(), application.getApiKey());
        Subscription subscription = subscriptionService.getSubscriptionById(request.getSubscriptionId());

        paymentService.pay(subscription.getCustomer().getCustomerId(), application.getId(), subscription.getSubscriptionPlan().getId(),
                subscription.getSubscriptionPlan().getPrice(), PaymentStatus.COMPLETED);
        return ResponseEntity.ok().build();
    }
}





