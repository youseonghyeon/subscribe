package com.example.subscribify.service.subscribe;

import com.example.subscribify.dto.service.EnrollSubscriptionServiceRequest;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.*;
import com.example.subscribify.repository.SubscriptionRepository;
import com.example.subscribify.util.SetupTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Import(SetupTestUtils.class)
class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SetupTestUtils setupTestUtils;

    @Test
    @DisplayName("구독 서비스 등록 성공 케이스")
    void enrollSubscriptionSuccessCase() {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        Customer customer = setupTestUtils.createCustomer(application);
        EnrollSubscriptionServiceRequest serviceRequest = new EnrollSubscriptionServiceRequest(customer, subscriptionPlan.getId());

        //when
        EnrollSubscriptionServiceResponse serviceResponse = subscriptionService.enrollInSubscription(customer.getCustomerId(), subscriptionPlan.getId(), application.getApiKey());

        // then
        Subscription subscription = subscriptionRepository.findById(serviceResponse.getSubscriptionId())
                .orElseThrow(() -> new IllegalStateException("Invalid subscription ID: " + serviceResponse.getSubscriptionId()));
        assertSubscriptionMatchesPlan(subscription, subscriptionPlan);
    }


    @Test
    @DisplayName("구독 서비스 취소 성공 케이스")
    void cancelSubscriptionSuccessCase() {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        Customer customer = setupTestUtils.createCustomer(application);
        Subscription subscription = setupTestUtils.createSubscription(subscriptionPlan, customer);


        // when
        subscriptionService.cancelSubscribe(subscription.getId());

        // then
        Subscription findSubscription = subscriptionRepository.findById(subscription.getId())
                .orElseThrow(() -> new IllegalStateException("Invalid subscription ID: " + subscription.getId()));
        assertEquals(SubscriptionStatus.CANCELED, findSubscription.getStatus());
    }

    private void assertSubscriptionMatchesPlan(Subscription subscription, SubscriptionPlan plan) {
        assertEquals(plan.getPlanName(), subscription.getSubscribeName());
        assertEquals(SubscriptionStatus.PENDING, subscription.getStatus());
        assertEquals(plan.getPrice(), subscription.getPrice());
    }

}
