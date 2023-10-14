package com.example.subscribify.api;

import com.example.subscribify.domain.AuthApplication;
import com.example.subscribify.dto.api.*;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceRequest;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.*;
import com.example.subscribify.service.customer.CustomerService;
import com.example.subscribify.service.subscribe.SubscriptionService;
import com.example.subscribify.service.subscriptionplan.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class SubscriptionApiController {

    private final CustomerService customerService;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPlanService subscriptionPlanService;

    /**
     * 구독 등록 신청 api
     * 구독을 하는 사용자(고객사의 사용자)의 ID(unique)와 구독할 Plan 의 ID를 받아서 구독을 처리한다.
     * 만약 고객사가 다루고 있는 사용자의 ID가 현재 시스템에는 등록이 안되어있다면, 사용자 데이터를 신규 생성한 후 구독을 처리한다.
     *
     * @param request
     * @param application
     * @return
     */
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

    /**
     * 구독 활성화 API
     *
     * @param request
     * @param application
     * @return
     */
    @PostMapping("/activate")
    public ResponseEntity<Void> activateSubscription(
            @RequestBody ActivateSubscriptionRequest request,
            @AuthApplication Application application) {

        subscriptionService.activateSubscribe(request.getSubscriptionId(), application.getApiKey());
        return ResponseEntity.ok().build();
    }


    /**
     * 고객사의 모든 고객 정보를 조회하는 API
     *
     * @param application
     * @return
     */
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerInfoResponse>> listAllCustomers(@AuthApplication Application application) {
        List<Customer> customers = customerService.getCustomersByApplicationId(application.getId());

        if (customers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CustomerInfoResponse> results = customers.stream().map(CustomerInfoResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    /**
     * 고객사의 특정 고객 정보를 조회하는 API
     * 해당 고객의 구독Plan 정보 및 구독 상태를 함께 조회한다.
     *
     * @param customerId
     * @param application
     * @return
     */
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<List<SubscriptionDetail>> getCustomerDetails(
            @PathVariable("customerId") String customerId,
            @AuthApplication Application application) {

        Customer customer = customerService.getCustomerByCustomerIdAndApplicationIdWithSubscriptions(customerId, application.getId());

        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        List<Subscription> subscriptions = customer.getSubscriptions();
        List<SubscriptionDetail> subscriptionDetails = subscriptions.stream()
                .map(SubscriptionDetail::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(subscriptionDetails);
    }

    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlanInfo>> getSubscriptionPlans(@AuthApplication Application application) {
        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanService.getSubscriptionPlanByApplicationId(application.getId());

        if (subscriptionPlans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<SubscriptionPlanInfo> results =
                subscriptionPlans.stream().map(SubscriptionPlanInfo::new).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }



}





