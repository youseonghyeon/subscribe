package com.example.subscribify.service.subscriptionplan;

import com.example.subscribify.dto.CreateSubscribeDto;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.repository.SubscriptionPlanRepository;
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

    @Test
    @Transactional
    @DisplayName("구독 Plan 생성 성공 케이스")
    void createSubscribePlan() {
        //when
        CreateSubscribeDto subscribeDto = createTestPlanDto();
        Long planId = subscriptionPlanService.createSubscribePlan(subscribeDto);
        //then
        assertTrue(subscriptionPlanRepository.findById(planId).isPresent());
    }


    @Test
    @Transactional
    @DisplayName("구독 Plan 수정 성공 케이스")
    void updateSubscribePlan() {
        //given
        CreateSubscribeDto subscribeDto = createTestPlanDto();
        Long planId = subscriptionPlanService.createSubscribePlan(subscribeDto);

        CreateSubscribeDto updatePlan = new CreateSubscribeDto("product plan", 2, "year",
                50000L, 0D, "none", 50000L);

        //when
        subscriptionPlanService.updateSubscribePlan(planId, updatePlan);

        //then
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new AssertionError("Plan should exist"));
        assertEquals(updatePlan.getSubscribeName(), subscriptionPlan.getSubscribeName());
        assertEquals(updatePlan.getDuration(), subscriptionPlan.getDuration());
        assertEquals(updatePlan.getDurationUnit(), subscriptionPlan.getDurationUnit());
        assertEquals(updatePlan.getPrice(), subscriptionPlan.getPrice());
        assertEquals(updatePlan.getDiscount(), subscriptionPlan.getDiscount());
        assertEquals(updatePlan.getDiscountType(), subscriptionPlan.getDiscountType());
        assertEquals(updatePlan.getDiscountedPrice(), subscriptionPlan.getDiscountedPrice());
    }

    @Test
    @Transactional
    @DisplayName("구독 Plan 삭제 성공 케이스")
    void deleteSubscribePlan() {
    }

    private CreateSubscribeDto createTestPlanDto() {
        return new CreateSubscribeDto("test plan", 1, "month",
                10000L, 0D, "none", 10000L);
    }
}
