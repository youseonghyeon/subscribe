package com.example.subscribify.service.subscribe;

import com.example.subscribify.entity.*;
import com.example.subscribify.repository.SubscriptionRepository;
import com.example.subscribify.repository.CustomerRepository;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private Long subscriptionPlanId;
    private Customer testCustomer;

    private static final String TEST_CUSTOMER_ID = "testClientId";
    private static final Long TEST_APPLICATION_ID = 12L;
    private static final String TEST_PLAN_NAME = "testPlanName";

    @BeforeEach
    void setUp() {
        setupTestSubscriptionPlan();
        testCustomer = setupTestCustomer();
    }

    @Test
    @DisplayName("Purchase subscription service successfully (Payment and start are processed separately)")
    void shouldPurchaseSubscriptionSuccessfully() {
        // given
        SubscriptionPlan subscriptionPlan = findSubscriptionPlanByIdOrThrow(subscriptionPlanId);

        // when
        Long subscriptionId = subscriptionService.purchaseSubscribe(testCustomer, subscriptionPlan.getId());

        // then
        Subscription subscription = findSubscriptionByIdOrThrow(subscriptionId);
        assertSubscriptionMatchesPlan(subscription, subscriptionPlan);
        assertEquals(testCustomer.getId(), subscription.getCustomer().getId());
    }

    @Test
    @DisplayName("Successfully cancel subscription service")
    void shouldCancelSubscriptionSuccessfully() {
        // given
        Long subscriptionId = subscriptionService.purchaseSubscribe(testCustomer, subscriptionPlanId);

        // when
        subscriptionService.cancelSubscribe(subscriptionId);

        // then
        Subscription subscription = findSubscriptionByIdOrThrow(subscriptionId);
        assertEquals(SubscriptionStatus.CANCELED, subscription.getStatus());
    }

    private void setupTestSubscriptionPlan() {
        SubscriptionPlan subscriptionPlan = SubscriptionPlan.builder()
                .planName(TEST_PLAN_NAME)
                .duration(1)
                .durationUnit(DurationUnit.MONTH)
                .price(1000L)
                .discount(0.1)
                .discountType(DiscountUnit.PERCENT)
                .discountedPrice(900L)
                .build();

        subscriptionPlanRepository.save(subscriptionPlan);
        subscriptionPlanId = subscriptionPlan.getId();
    }

    private Customer setupTestCustomer() {
        Customer customer = Customer.builder()
                .customerId(TEST_CUSTOMER_ID)
                .applicationId(TEST_APPLICATION_ID)
                .build();

        return customerRepository.save(customer);
    }

    private SubscriptionPlan findSubscriptionPlanByIdOrThrow(Long id) {
        return subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Invalid subscription plan ID: " + id));
    }

    private Subscription findSubscriptionByIdOrThrow(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Invalid customer subscription ID: " + id));
    }

    private void assertSubscriptionMatchesPlan(Subscription subscription, SubscriptionPlan plan) {
        assertEquals(plan.getPlanName(), subscription.getSubscribeName());
        assertEquals(SubscriptionStatus.PENDING, subscription.getStatus());
        assertEquals(plan.getPrice(), subscription.getPrice());
        assertEquals(plan.getDiscountedPrice(), subscription.getDiscountedPrice());
    }

}
