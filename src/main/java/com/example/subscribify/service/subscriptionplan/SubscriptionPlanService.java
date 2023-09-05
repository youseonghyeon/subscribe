package com.example.subscribify.service.subscriptionplan;

import com.example.subscribify.dto.CreateSubscribeDto;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;

    /**
     * 구독 Plan 생성
     *
     * @param createSubscribeDto
     * @return
     */
    public Long createSubscribePlan(CreateSubscribeDto createSubscribeDto) {
        SubscriptionPlan newPlan = SubscriptionPlan.builder()
                .subscribeName(createSubscribeDto.getSubscribeName())
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
     * @param id
     * @param createSubscribeDto TODO 수정을 위한 DTO 생성
     * @return
     */
    @Transactional
    public void updateSubscribePlan(Long id, CreateSubscribeDto createSubscribeDto) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription plan ID: " + id));

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


}
