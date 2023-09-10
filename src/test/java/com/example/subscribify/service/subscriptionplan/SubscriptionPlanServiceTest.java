package com.example.subscribify.service.subscriptionplan;

import com.example.subscribify.dto.CreateSubscribeDto;
import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.dto.UpdateSubscribeDto;
import com.example.subscribify.entity.DiscountUnit;
import com.example.subscribify.entity.DurationUnit;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    @Transactional
    @DisplayName("구독 Plan 생성 성공 케이스")
    void createSubscribePlanSuccessCase() {
        //given
        User user = mockUser();
        //when
        CreateSubscribeDto subscribeDto = createTestPlanDto();
        Long planId = subscriptionPlanService.createSubscribePlan(subscribeDto, user);
        //then
        assertTrue(subscriptionPlanRepository.findById(planId).isPresent());
    }


    @Test
    @Transactional
    @DisplayName("구독 Plan 수정 성공 케이스")
    void updateSubscribePlanSuccessCase() {
        //given
        User user = mockUser();
        CreateSubscribeDto subscribeDto = createTestPlanDto();
        Long planId = subscriptionPlanService.createSubscribePlan(subscribeDto, user);

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
    @DisplayName("구독 Plan 삭제 성공 케이스")
    void deleteSubscribePlanSuccessCase() {
        //given
        User user = mockUser();
        CreateSubscribeDto subscribeDto = createTestPlanDto();
        Long planId = subscriptionPlanService.createSubscribePlan(subscribeDto, user);
        //when
        subscriptionPlanService.deleteSubscribePlan(planId);
        //then
        assertFalse(subscriptionPlanRepository.findById(planId).isPresent());
    }

    private CreateSubscribeDto createTestPlanDto() {
        return new CreateSubscribeDto("test plan", 1, DurationUnit.MONTH,
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
}
