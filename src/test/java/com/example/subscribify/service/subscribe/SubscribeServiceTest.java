package com.example.subscribify.service.subscribe;

import com.example.subscribify.dto.PurchaseDto;
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
class SubscribeServiceTest {

    @Autowired
    private SubscribeService subscribeService;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private Long subscriptionPlanId;

    private static final String TEST_CUSTOMER_ID = "testClientId";
    private static final String TEST_PARTNER_ID = "testPartnerId";
    private static final String TEST_PLAN_NAME = "testPlanName";



    @BeforeEach
    void setUp() {
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

        Customer customer = Customer.builder()
                .customerId(TEST_CUSTOMER_ID)
                .partnerId(TEST_PARTNER_ID)
                .build();

        customerRepository.save(customer);
    }

    @Test
    @DisplayName("구독 서비스 구매 성공 케이스 **(결제와 구독 시작은 별도로 처리)**")
    void purchaseSubscribeSuccessCase() {
        // given
        Customer customer = customerRepository.findByCustomerId(TEST_CUSTOMER_ID)
                .orElseThrow(() -> new IllegalStateException("Invalid client ID: testClientId"));
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(subscriptionPlanId)
                .orElseThrow(() -> new IllegalStateException("Invalid subscription plan ID: " + subscriptionPlanId));

        //when
        Long subscriptionId = subscribeService.purchaseSubscribe(customer, new PurchaseDto(subscriptionPlan.getId()));

        //then
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalStateException("Invalid customer subscription ID: " + subscriptionId));
        assertEquals(subscriptionPlan.getPlanName(), subscription.getSubscribeName());
        assertEquals(SubscriptionStatus.PENDING, subscription.getStatus());
        assertEquals(subscriptionPlan.getPrice(), subscription.getPrice());
        assertEquals(subscriptionPlan.getDiscountedPrice(), subscription.getDiscountedPrice());
        assertEquals(customer.getId(), subscription.getCustomer().getId());
    }

    @Test
    @DisplayName("구독 서비스 취소 성공 케이스")
    void cancelSubscribeSuccessCase() {
        // given
        Customer customer = customerRepository.findByCustomerId(TEST_CUSTOMER_ID)
                .orElseThrow(() -> new IllegalStateException("Invalid client ID: testClientId"));
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(subscriptionPlanId)
                .orElseThrow(() -> new IllegalStateException("Invalid subscription plan ID: " + subscriptionPlanId));
        Long subscriptionId = subscribeService.purchaseSubscribe(customer, new PurchaseDto(subscriptionPlan.getId()));

        //when
        subscribeService.cancelSubscribe(subscriptionId);

        //then
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalStateException("Invalid customer subscription ID: " + subscriptionId));
        assertEquals(SubscriptionStatus.CANCELED, subscription.getStatus());
    }


}
