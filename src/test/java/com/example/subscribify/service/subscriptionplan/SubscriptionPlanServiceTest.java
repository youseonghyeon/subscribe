package com.example.subscribify.service.subscriptionplan;

import com.example.subscribify.config.security.CustomUserDetails;
import com.example.subscribify.dto.controller.CreateSubscribeDto;
import com.example.subscribify.dto.controller.UpdateSubscribeDto;
import com.example.subscribify.entity.*;
import com.example.subscribify.repository.ApplicationRepository;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.repository.UserRepository;
import com.example.subscribify.util.SetupTestUtils;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(SetupTestUtils.class)
class SubscriptionPlanServiceTest {

    @Autowired
    SubscriptionPlanService subscriptionPlanService;
    @Autowired
    SubscriptionPlanRepository subscriptionPlanRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    SetupTestUtils setupTestUtils;

    @Test
    @Transactional
    @DisplayName("구독 Plan 생성 성공 케이스")
    void createSubscribePlanSuccessCase() {
        //given
        User user = mockUser();
        Application application = mockApplication(user);

        //when
        CreateSubscribeDto subscribeDto = createTestPlanDto();
        Long planId = subscriptionPlanService.createSubscriptionPlan(subscribeDto, application);
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
        Long planId = subscriptionPlanService.createSubscriptionPlan(subscribeDto, application);

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
        Long planId = subscriptionPlanService.createSubscriptionPlan(subscribeDto, application);
        //when
        subscriptionPlanService.deleteSubscribePlan(planId);
        //then
        assertFalse(subscriptionPlanRepository.findById(planId).isPresent());
    }

    @Test
    @DisplayName("구독 plan 조회 성공 케이스")
    void getSubscribePlanSuccessCase() {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        //when
        SubscriptionPlan findPlan = subscriptionPlanService.getSubscribePlan(subscriptionPlan.getId());
        //then
        assertNotNull(findPlan);
        assertSubscriptionPlanEquals(subscriptionPlan, findPlan);
    }

    @Test
    @DisplayName("구독 plan 조회 실패 케이스 (존재하지 않는 planId)")
    void getSubscribePlanFailCase() {
        //given
        Long planId = 1349871L;
        //then
        assertThrows(IllegalArgumentException.class, () -> subscriptionPlanService.getSubscribePlan(planId));
    }

    @Test
    @DisplayName("application 에 포함된 plan 전체 조회")
    void getSubscribePlansByApplication() {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        SubscriptionPlan plan1 = setupTestUtils.createSubscriptionPlan(application);
        SubscriptionPlan plan2 = setupTestUtils.createSubscriptionPlan(application);
        //given - another application
        User user2 = setupTestUtils.createUser();
        Application application2 = setupTestUtils.createApplication(user2);
        SubscriptionPlan plan3 = setupTestUtils.createSubscriptionPlan(application2);

        //when
        List<SubscriptionPlan> findPlans = subscriptionPlanService.getSubscriptionPlanByApplicationId(application.getId());
        //then
        assertEquals(2, findPlans.size());
        findPlans.stream().filter(plan  -> plan.getId().equals(plan1.getId()))
                .findAny()
                .ifPresentOrElse(findPlan -> assertSubscriptionPlanEquals(plan1, findPlan), () -> fail("plan1 should be in the list"));
        findPlans.stream().filter(plan  -> plan.getId().equals(plan2.getId()))
                .findFirst()
                .ifPresentOrElse(findPlan -> assertSubscriptionPlanEquals(plan2, findPlan), () -> fail("plan2 should be in the list"));
    }

    private static void assertSubscriptionPlanEquals(SubscriptionPlan subscriptionPlan, SubscriptionPlan findPlan) {
        assertAll(
                () -> assertEquals(subscriptionPlan.getId(), findPlan.getId(), "id should be equal"),
                () -> assertEquals(subscriptionPlan.getPlanName(), findPlan.getPlanName(), "plan name should be equal"),
                () -> assertEquals(subscriptionPlan.getDuration(), findPlan.getDuration(), "duration should be equal"),
                () -> assertEquals(subscriptionPlan.getDurationUnit(), findPlan.getDurationUnit(), "duration unit should be equal"),
                () -> assertEquals(subscriptionPlan.getPrice(), findPlan.getPrice(), "price should be equal"),
                () -> assertEquals(subscriptionPlan.getDiscount(), findPlan.getDiscount(), "discount should be equal"),
                () -> assertEquals(subscriptionPlan.getDiscountType(), findPlan.getDiscountType(), "discount type should be equal"),
                () -> assertEquals(subscriptionPlan.getDiscountedPrice(), findPlan.getDiscountedPrice(), "discounted price should be equal")
        );
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
