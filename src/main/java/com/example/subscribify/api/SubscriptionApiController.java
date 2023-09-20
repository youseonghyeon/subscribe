package com.example.subscribify.api;

import com.example.subscribify.domain.AuthApplication;
import com.example.subscribify.dto.api.ActivateSubscriptionRequest;
import com.example.subscribify.dto.api.EnrollSubscriptionRequest;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceRequest;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.*;
import com.example.subscribify.service.customer.CustomerService;
import com.example.subscribify.service.subscribe.SubscriptionService;
import com.example.subscribify.service.subscriptionplan.SubscriptionPlanService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubscriptionApiController {

    private final CustomerService customerService;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPlanService subscriptionPlanService;

    @PostMapping("/enroll")
    public ResponseEntity<EnrollSubscriptionServiceResponse> enrollSubscription(
            @RequestBody EnrollSubscriptionRequest request,
            @AuthApplication Application application) {

        Customer customer = customerService.getOrCreateCustomer(request.getCustomerId(), application.getId());

        EnrollSubscriptionServiceRequest serviceRequest =
                new EnrollSubscriptionServiceRequest(customer, request.getPlanId());

        EnrollSubscriptionServiceResponse serviceResponse =
                subscriptionService.enrollSubscribe(serviceRequest, application.getApiKey());

        if (serviceResponse.hasError()) {
            return ResponseEntity.badRequest().body(serviceResponse);
        }

        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activateSubscription(
            @RequestBody ActivateSubscriptionRequest request,
            @AuthApplication Application application) {

        subscriptionService.activateSubscribe(request.getSubscriptionId(), application.getApiKey());
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/enrollAndActivate")
//    public ResponseEntity<?> enrollAndActivateSubscription(
//            @RequestBody EnrollAndActivateSubscriptionRequest request,
//            @AuthApplication Application application) {
//
//        // 사용자 등록
//        Customer customer = customerService.getOrCreateCustomer(request.getCustomerId(), application.getId());
//
//        EnrollSubscriptionServiceRequest serviceRequest =
//                new EnrollSubscriptionServiceRequest(customer, request.getPlanId());
//
//        EnrollSubscriptionServiceResponse serviceResponse =
//                subscriptionService.enrollSubscribe(serviceRequest, application.getApiKey());
//
//        if (serviceResponse.hasError()) {
//            return ResponseEntity.badRequest().body(serviceResponse);
//        }
//
//        // 사용자 서비스 활성화
//        subscriptionService.activateSubscribe(serviceResponse.getSubscriptionId(), application.getApiKey());
//
//        return ResponseEntity.ok(serviceResponse);
//    }
//
//    public class EnrollAndActivateSubscriptionRequest {
//        private String customerId;
//        private String planId;
//
//        // getters, setters, etc.
//    }


    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResult>> listAllCustomers(@AuthApplication Application application) {
        List<Customer> customers = customerService.getCustomersByApplicationId(application.getId());

        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CustomerResult> results = customers.stream().map(CustomerResult::new).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<Customer> getCustomerDetails(
            @PathVariable("customerId") String customerId,
            @AuthApplication Application application) {

        Customer customer = customerService.getCustomerByCustomerIdAndApplicationId(customerId, application.getId());

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlanResult>> getSubscriptionPlans(@AuthApplication Application application) {
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanService.getSubscriptionPlanByApplicationId(application.getId());

        if (subscriptionPlans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<SubscriptionPlanResult> results =
                subscriptionPlans.stream().map(SubscriptionPlanResult::new).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }


    @Data
    private static class CustomerResult {
        private final Long id;
        private final String customerId;
        private final Long applicationId;

        public CustomerResult(Customer customer) {
            this.id = customer.getId();
            this.customerId = customer.getCustomerId();
            this.applicationId = customer.getApplicationId();
        }
    }


    @Data
    @AllArgsConstructor
    private static class SubscriptionPlanResult {
        private final Long id;
        private final String planName;
        private final Integer duration;
        private final DurationUnit durationUnit;
        private final Long price;
        private final Double discount;
        private final DiscountUnit discountType;
        private final Long discountedPrice;

        public SubscriptionPlanResult(SubscriptionPlan subscriptionPlan) {
            this.id = subscriptionPlan.getId();
            this.planName = subscriptionPlan.getPlanName();
            this.duration = subscriptionPlan.getDuration();
            this.durationUnit = subscriptionPlan.getDurationUnit();
            this.price = subscriptionPlan.getPrice();
            this.discount = subscriptionPlan.getDiscount();
            this.discountType = subscriptionPlan.getDiscountType();
            this.discountedPrice = subscriptionPlan.getDiscountedPrice();
        }
    }
}





