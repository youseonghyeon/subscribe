package com.example.subscribify.controller;

import com.example.subscribify.domain.AuthApplication;
import com.example.subscribify.dto.api.ActivateSubscriptionRequest;
import com.example.subscribify.dto.api.EnrollSubscriptionRequest;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceRequest;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.*;
import com.example.subscribify.service.customer.CustomerService;
import com.example.subscribify.service.subscribe.SubscriptionService;
import com.example.subscribify.service.subscriptionplan.SubscriptionPlanService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SubscriptionApiController {

    private final CustomerService customerService;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPlanService subscriptionPlanService;


    @PostMapping("/api/enroll")
    public ResponseEntity<EnrollSubscriptionServiceResponse> enrollSubscription(@RequestBody EnrollSubscriptionRequest request, @AuthApplication Application application) {
        Customer customer = customerService.getOrCreateCustomer(request.getCustomerId(), application.getId());
        // 구독 등록
        EnrollSubscriptionServiceRequest serviceRequest = new EnrollSubscriptionServiceRequest(customer, request.getPlanId());
        EnrollSubscriptionServiceResponse serviceResponse = subscriptionService.enrollSubscribe(serviceRequest, application.getApiKey());
        if (StringUtils.hasText(serviceResponse.getErrorMessage())) {
            return new ResponseEntity<>(serviceResponse, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
    }

    @PostMapping("/api/activate")
    public void activateSubscription(@RequestBody ActivateSubscriptionRequest request, @AuthApplication Application application) {
        subscriptionService.activateSubscribe(request.getSubscriptionId(), application.getApiKey());
    }

    @GetMapping("/api/customers")
    public ResponseEntity<List<CustomerResult>> listAllCustomers(@AuthApplication Application application) {
        List<Customer> customers = customerService.getCustomersByApplicationId(application.getId());
        if (customers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<CustomerResult> results = customers.stream().map(CustomerResult::new).collect(Collectors.toList());
        return new ResponseEntity<>(results, HttpStatus.OK);
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

    @GetMapping("/api/customers/{customerId}")
    public ResponseEntity<Customer> getCustomerDetails(@PathVariable("customerId") String customerId, @AuthApplication Application application) {
        Customer customer = customerService.getCustomerByCustomerIdAndApplicationId(customerId, application.getId());
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/api/subscriptionplans")
    public ResponseEntity<List<SubscriptionPlanResult>> getSubscriptionPlans(@AuthApplication Application application) {
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanService.getSubscriptionPlanByApplicationId(application.getId());
        if (subscriptionPlans.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<SubscriptionPlanResult> results = subscriptionPlans.stream().map(SubscriptionPlanResult::new).collect(Collectors.toList());
        return new ResponseEntity<>(results, HttpStatus.OK);
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
