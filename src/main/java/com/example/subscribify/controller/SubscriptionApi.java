package com.example.subscribify.controller;

import com.example.subscribify.config.security.ApiKeyGenerator;
import com.example.subscribify.dto.ActivateSubscriptionRequest;
import com.example.subscribify.dto.EnrollSubscriptionRequest;
import com.example.subscribify.entity.Customer;
import com.example.subscribify.service.customer.CustomerService;
import com.example.subscribify.service.subscribe.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SubscriptionApi {

    private final CustomerService customerService;
    private final SubscriptionService subscriptionService;


    @PostMapping("/api/enroll")
    public void enrollSubscription(@RequestBody EnrollSubscriptionRequest request) {
        Customer customer = customerService.getOrCreateCustomer(request.getCustomerId(), request.getApplicationId());
        // 구독 등록
        subscriptionService.purchaseSubscribe(customer, request.getPlanId());
    }

    @PostMapping("/api/activate")
    public void activateSubscription(@RequestBody ActivateSubscriptionRequest request) {
        subscriptionService.activateSubscribe(request.getSubscriptionId());
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/generate-apikey")
    public String generateApiKey() {
        return ApiKeyGenerator.generateApiKey(32);
    }

//    @GetMapping("/api/plan-list")
//    public List<SubscriptionPlan> getPlanList(@RequestBody String apiKey) {
//        Optional<User> byApiKey = userRepository.findByApiKey(apiKey);
//        if (byApiKey.isEmpty()) {
//            throw new IllegalArgumentException("Invalid API Key");
//        }
//        return byApiKey.get().ge;
//    }

}
