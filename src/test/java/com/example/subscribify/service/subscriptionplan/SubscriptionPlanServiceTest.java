package com.example.subscribify.service.subscriptionplan;

import com.example.subscribify.dto.controller.CreateSubscribeDto;
import com.example.subscribify.dto.controller.UpdateSubscribeDto;
import com.example.subscribify.entity.*;
import com.example.subscribify.repository.ApplicationRepository;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.repository.UserRepository;
import com.example.subscribify.config.security.CustomUserDetails;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubscriptionPlanServiceTest {

    @Autowired
    SubscriptionPlanService subscriptionPlanService;
    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicationRepository applicationRepository;

    @Test
    @Transactional
    @DisplayName("구독 Plan 생성 성공 케이스")
    void createSubscribePlanSuccessCase() {
        //given
        User user = mockUser();
        Application application = mockApplication(user);

        //when
        CreateSubscribeDto subscribeDto = createTestPlanDto();
        Long planId = subscriptionPlanService.createSubscribePlan(subscribeDto, application);
        //then
        assertTrue(subscriptionPlanRepository.findById(planId).isPresent());
    }

    private Application mockApplication(User user) {
        return applicationRepository.save(Application.builder()
                .name("test application")
                .apiKey("test api key")
                .secretKey("test secret key")
                .user(user)
                .build());
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    @Test
    @Transactional
    @DisplayName("구독 Plan 수정 성공 케이스")
    void updateSubscribePlanSuccessCase() {
        //given
        User user = mockUser();
        Application application = mockApplication(user);
        login(user);

        CreateSubscribeDto subscribeDto = createTestPlanDto();
        Long planId = subscriptionPlanService.createSubscribePlan(subscribeDto, application);

        UpdateSubscribeDto updateSubscribeDto = new UpdateSubscribeDto(planId, "product plan", 2, DurationUnit.MONTH,
                50000L, 0D, DiscountUnit.NONE, 50000L);

        //when
        subscriptionPlanService.updateSubscribePlan(planId, updateSubscribeDto);

        //then
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new AssertionError("Plan should exist"));
        assertEquals(updateSubscribeDto.getSubscribeName(), subscriptionPlan.getPlanName());
        assertEquals(updateSubscribeDto.getDuration(), subscriptionPlan.getDuration());
        assertEquals(updateSubscribeDto.getDurationUnit(), subscriptionPlan.getDurationUnit());
        assertEquals(updateSubscribeDto.getPrice(), subscriptionPlan.getPrice());
        assertEquals(updateSubscribeDto.getDiscount(), subscriptionPlan.getDiscount());
        assertEquals(updateSubscribeDto.getDiscountType(), subscriptionPlan.getDiscountType());
        assertEquals(updateSubscribeDto.getDiscountedPrice(), subscriptionPlan.getDiscountedPrice());
    }

    @Test
    @Transactional
    @WithMockUser(username = "")
    @DisplayName("구독 Plan 삭제 성공 케이스")
    void deleteSubscribePlanSuccessCase() {
        //given
        User user = mockUser();
        login(user);
        Application application = mockApplication(user);

        CreateSubscribeDto subscribeDto = createTestPlanDto();
        Long planId = subscriptionPlanService.createSubscribePlan(subscribeDto, application);
        //when
        subscriptionPlanService.deleteSubscribePlan(planId);
        //then
        assertFalse(subscriptionPlanRepository.findById(planId).isPresent());
    }

    private CreateSubscribeDto createTestPlanDto() {
        return new CreateSubscribeDto(null, "test plan", 1, DurationUnit.MONTH,
                10000L, 0D, DiscountUnit.NONE, 10000L);
    }

    private User mockUser() {
        return userRepository.save(User.builder()
                .username("username")
                .password("password")
                .email("testEmail@email.com")
                .firstName("firstName")
                .lastName("lastName")
                .address("address")
                .city("city")
                .state("state")
                .zip("zip")
                .country("country")
                .build());
    }


    private void login(User user) {
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


}
