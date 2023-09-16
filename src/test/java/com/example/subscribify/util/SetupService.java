package com.example.subscribify.util;

import com.example.subscribify.entity.*;
import com.example.subscribify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.time.LocalDateTime;

@TestComponent
public class SetupService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserRepository userRepository;

    public User createUser() {
        return userRepository.save(User.builder()
                .id(1L)
                .username("testUser")
                .build()
        );
    }

    public Application createApplication(User user) {
        return applicationRepository.save(Application.builder()
                .id(1L)
                .name("testApplication")
                .apiKey("testApiKey")
                .secretKey("testSecretKey")
                .user(user)
                .build());
    }

    public SubscriptionPlan createSubscriptionPlan(Application application) {
        return subscriptionPlanRepository.save(SubscriptionPlan.builder()
                .id(1L)
                .planName("testPlan")
                .duration(1)
                .durationUnit(DurationUnit.MONTH)
                .price(1000L)
                .discount(0.1)
                .discountType(DiscountUnit.PERCENT)
                .discountedPrice(900L)
                .application(application)
                .build());
    }

    public Customer createCustomer(Application application) {
        Customer customer = Customer.builder()
                .id(1L)
                .customerId("testCustomerId")
                .applicationId(application.getId())
                .build();
        return customerRepository.save(customer);
    }

    public Subscription createSubscription(SubscriptionPlan plan, Customer customer) {
        return subscriptionRepository.save(Subscription.builder()
                .id(1L)
                .subscribeName(plan.getPlanName())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(plan.getDuration()))
                .durationMonth(plan.getDuration())
                .status(SubscriptionStatus.PENDING)
                .price(plan.getPrice())
                .discountRate(plan.getDiscount())
                .discountedPrice(plan.getDiscountedPrice())
                .customer(customer)
                .subscriptionPlan(plan)
                .build());
    }
}
