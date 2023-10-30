package com.example.subscribify.util;

import com.example.subscribify.entity.*;
import com.example.subscribify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Random;

@TestComponent
public class SetupTestUtils {

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
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser() {
        return this.createUser("testUser");
    }
    public User createUser(String username) {
        long randomId = new Random().nextLong();
        return userRepository.save(User.builder()
                .id(randomId)
                .username(username)
                .password(passwordEncoder.encode("testPassword"))
                .email("testEmail@mail.com")
                .build()
        );
    }

    public Application createApplication(User user) {
        long randomId = new Random().nextLong();
        return applicationRepository.save(Application.builder()
                .id(randomId)
                .name("testApplication")
                .apiKey("testApiKey")
                .secretKey("testSecretKey")
                .user(user)
                .build());
    }

    public SubscriptionPlan createSubscriptionPlan(Application application) {
        long randomId = new Random().nextLong();
        return subscriptionPlanRepository.save(SubscriptionPlan.builder()
                .id(randomId)
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
        long randomId = new Random().nextLong();
        Customer customer = Customer.builder()
                .id(randomId)
                .customerId("testCustomerId")
                .applicationId(application.getId())
                .build();
        return customerRepository.save(customer);
    }

    public Subscription createSubscription(SubscriptionPlan plan, Customer customer) {
        long randomId = new Random().nextLong();
        return subscriptionRepository.save(Subscription.builder()
                .id(randomId)
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
