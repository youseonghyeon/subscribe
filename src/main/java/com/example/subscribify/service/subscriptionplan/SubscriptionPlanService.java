package com.example.subscribify.service.subscriptionplan;

import com.example.subscribify.dto.CreateSubscribeDto;
import com.example.subscribify.dto.UpdateSubscribeDto;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.service.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
    public Long createSubscribePlan(CreateSubscribeDto createSubscribeDto, User createUser) {
        SubscriptionPlan newPlan = SubscriptionPlan.builder()
                .planName(createSubscribeDto.getSubscribeName())
                .duration(createSubscribeDto.getDuration())
                .durationUnit(createSubscribeDto.getDurationUnit())
                .price(createSubscribeDto.getPrice())
                .discount(createSubscribeDto.getDiscount())
                .discountType(createSubscribeDto.getDiscountType())
                .discountedPrice(createSubscribeDto.getDiscountedPrice())
                .user(createUser)
                .build();
        return subscriptionPlanRepository.save(newPlan).getId();
    }

    /**
     * 구독 Plan 수정
     *
     * @param planId             수정할 Plan ID
     * @param updateSubscribeDto TODO 수정을 위한 DTO 생성
     */
    @Transactional
    public void updateSubscribePlan(Long planId, UpdateSubscribeDto updateSubscribeDto) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription plan ID: " + planId));

        checkUserAuthorization(subscriptionPlan.getUser().getId());

        subscriptionPlan.update(updateSubscribeDto.getSubscribeName(), updateSubscribeDto.getDuration(),
                updateSubscribeDto.getDurationUnit(), updateSubscribeDto.getPrice(), updateSubscribeDto.getDiscount(),
                updateSubscribeDto.getDiscountType(), updateSubscribeDto.getDiscountedPrice());
    }


    /**
     * 구독 Plan 삭제
     *
     * @param planId 삭제할 Plan ID
     */
    public void deleteSubscribePlan(Long planId) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription plan ID: " + planId));

        checkUserAuthorization(subscriptionPlan.getUser().getId());
        subscriptionPlanRepository.deleteById(planId);
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
    public List<SubscriptionPlan> getMySubscribePlan(User user) {
        return subscriptionPlanRepository.findByUser(user);
    }


    /**
     * 인증된 사용자가 주어진 사용자 ID와 일치하는지 확인합니다.
     *
     * @param userIdContainsPlan 인증된 사용자와 비교할 사용자 ID입니다.
     */
    private void checkUserAuthorization(Long userIdContainsPlan) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userIdContainsPlan == null || authentication == null ||
                authentication instanceof AnonymousAuthenticationToken ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throwForbiddenException();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            if (!userIdContainsPlan.equals(customUserDetails.toUser().getId())) {
                throwForbiddenException();
            }
        } else {
            throwForbiddenException();
        }
    }

    private void throwForbiddenException() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 리소스에 액세스 할 권한이 없습니다.");
    }


}
