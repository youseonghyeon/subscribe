package com.example.subscribify.service.subscribe;

import com.example.subscribify.dto.service.EnrollSubscriptionServiceRequest;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.*;
import com.example.subscribify.repository.SubscriptionRepository;
import com.example.subscribify.util.SetupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Import(SetupService.class)
class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SetupService setupService;

    @Test
    @DisplayName("구독 서비스 등록 성공 케이스")
    void enrollSubscriptionSuccessCase() {
        //given
        User user = setupService.createUser();
        Application application = setupService.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupService.createSubscriptionPlan(application);
        Customer customer = setupService.createCustomer(application);
        EnrollSubscriptionServiceRequest serviceRequest = new EnrollSubscriptionServiceRequest(customer, subscriptionPlan.getId(), application.getApiKey(), application.getSecretKey());

        //when
        EnrollSubscriptionServiceResponse serviceResponse = subscriptionService.enrollSubscribe(serviceRequest);

        // then
        Subscription subscription = subscriptionRepository.findById(serviceResponse.getSubscriptionId())
                .orElseThrow(() -> new IllegalStateException("Invalid subscription ID: " + serviceResponse.getSubscriptionId()));
        assertSubscriptionMatchesPlan(subscription, subscriptionPlan);
    }


    @Test
    @DisplayName("구독 서비스 취소 성공 케이스")
    void cancelSubscriptionSuccessCase() {
        //given
        User user = setupService.createUser();
        Application application = setupService.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupService.createSubscriptionPlan(application);
        Customer customer = setupService.createCustomer(application);
        Subscription subscription = setupService.createSubscription(subscriptionPlan, customer);


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
        assertEquals(plan.getDiscountedPrice(), subscription.getDiscountedPrice());
    }

}
