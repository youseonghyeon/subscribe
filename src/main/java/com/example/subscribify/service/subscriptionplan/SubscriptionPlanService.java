package com.example.subscribify.service.subscriptionplan;

import com.example.subscribify.dto.CreateSubscribeDto;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    /**
     * 구독 Plan 생성
     *
     * @param createSubscribeDto 구독 Plan 생성을 위한 DTO
     * @return 생성된 구독 Plan ID
     */
    public Long createSubscribePlan(CreateSubscribeDto createSubscribeDto) {
        SubscriptionPlan newPlan = SubscriptionPlan.builder()
                .planName(createSubscribeDto.getSubscribeName())
                .duration(createSubscribeDto.getDuration())
                .durationUnit(createSubscribeDto.getDurationUnit())
                .price(createSubscribeDto.getPrice())
                .discount(createSubscribeDto.getDiscount())
                .discountType(createSubscribeDto.getDiscountType())
                .discountedPrice(createSubscribeDto.getDiscountedPrice())
                .build();
        return subscriptionPlanRepository.save(newPlan).getId();
    }

    /**
     * 구독 Plan 수정
     *
     * @param planId             수정할 Plan ID
     * @param createSubscribeDto TODO 수정을 위한 DTO 생성
     */
    @Transactional
    public void updateSubscribePlan(Long planId, CreateSubscribeDto createSubscribeDto) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription plan ID: " + planId));

        subscriptionPlan.update(createSubscribeDto.getSubscribeName(), createSubscribeDto.getDuration(),
                createSubscribeDto.getDurationUnit(), createSubscribeDto.getPrice(), createSubscribeDto.getDiscount(),
                createSubscribeDto.getDiscountType(), createSubscribeDto.getDiscountedPrice());
    }

    /**
     * 구독 Plan 삭제
     *
     * @param id
     */
    public void deleteSubscribePlan(Long id) {
        subscriptionPlanRepository.deleteById(id);
    }

    /**
     * 구독 Plan 조회
     */
    public SubscriptionPlan getSubscribePlan(Long id) {
        return subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription plan ID: " + id));
    }

    /**
     * 구독 Plan 전체 조회
     */
    public List<SubscriptionPlan> getAllSubscribePlan() {
        return subscriptionPlanRepository.findAll();
    }

    /**
     * 나의 구독 Plan 조회
     */
    public List<SubscriptionPlan> getMySubscribePlan(Long userId) {
        return subscriptionPlanRepository.findByUserId(userId);
    }
}
