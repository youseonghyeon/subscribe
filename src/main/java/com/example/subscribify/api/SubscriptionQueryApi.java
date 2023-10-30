package com.example.subscribify.api;

import com.example.subscribify.domain.AuthApplication;
import com.example.subscribify.dto.api.CustomerInfoResponse;
import com.example.subscribify.dto.api.SubscriptionDetail;
import com.example.subscribify.dto.api.SubscriptionPlanInfo;
import com.example.subscribify.entity.Application;
import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.Subscription;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.service.customer.CustomerService;
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
public class SubscriptionQueryApi {

    private final CustomerService customerService;
    private final SubscriptionPlanService subscriptionPlanService;


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
