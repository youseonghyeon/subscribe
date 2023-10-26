package com.example.subscribify.api;

import com.example.subscribify.domain.AuthApplication;
import com.example.subscribify.dto.api.*;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.*;
import com.example.subscribify.repository.SubscriptionRepository;
import com.example.subscribify.service.customer.CustomerService;
import com.example.subscribify.service.payment.PaymentService;
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
    private final PaymentService paymentService;
    private final SubscriptionRepository subscriptionRepository;

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
        // reasonable 한 값들만 등록이 되도록 하고 그렇지 않은 값들은 예외를 발생시킨다.
        // 1. customer 을 생성하고, 만약 기존에 있던 고객이면 그 고객을 가져온다.




        // 2. subscription 을 생성하고 등록한다. (activate 는 결제 로직에서 처리한다.)
        // application Option 에 따라서 중복 등록을 어떻게 처리할지 나눈다.
        // 전략 패턴을 사용하여 처리함. (새로운 옵션이 추가되더라도 코드 수정이 필요 없도록 하기 위함)
        // 만약 중복 처리 말고 다른 option이 추가되면 데코레이터 패턴을 이용해서 추가하자

        EnrollSubscriptionServiceResponse serviceResponse = subscriptionService.enrollSubscribe(request.getCustomerId(), request.getPlanId(), application.getApiKey(), application.getDuplicatePaymentOption());

        if (serviceResponse.hasError()) {
            return ResponseEntity.badRequest().body(serviceResponse);
        }

        return ResponseEntity.ok(serviceResponse);
    }

    @PostMapping("/eroll-and-pay")
    public ResponseEntity<EnrollSubscriptionServiceResponse> enrollAndPaySubscription() {
        return null;
    }

    /**
     * 구독 활성화 API
     *
     * @param request
     * @param application
     * @return
     */
    @PostMapping("/activate-pay")
    public ResponseEntity<Void> activateSubscription(
            @RequestBody ActivateSubscriptionRequest request,
            @AuthApplication Application application) {

        subscriptionService.activateSubscribe(request.getSubscriptionId(), application.getApiKey());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activateSubscriptionAndPayment(
            @RequestBody ActivateSubscriptionRequest request,
            @AuthApplication Application application) {

        subscriptionService.activateSubscribe(request.getSubscriptionId(), application.getApiKey());
        Subscription subscription = subscriptionRepository.findById(request.getSubscriptionId()).orElseThrow(() -> new IllegalStateException("Invalid subscription ID: " + request.getSubscriptionId()));

        paymentService.pay(subscription.getCustomer().getCustomerId(), application.getId(), subscription.getSubscriptionPlan().getId(),
                subscription.getSubscriptionPlan().getPrice(), PaymentStatus.COMPLETED);
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





