package com.example.subscribify.service.subscriptionplan;

import com.example.subscribify.config.security.CustomUserDetails;
import com.example.subscribify.dto.controller.CreateSubscribeDto;
import com.example.subscribify.dto.controller.UpdateSubscribeDto;
import com.example.subscribify.entity.Application;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.entity.SubscriptionStatus;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;

    /**
     * 구독 Plan 생성
     *
     * @param createSubscribeDto 구독 Plan 생성을 위한 DTO
     * @return 생성된 구독 Plan ID
     */
    @Transactional
    public Long createSubscriptionPlan(CreateSubscribeDto createSubscribeDto, Application application) {
        Objects.requireNonNull(createSubscribeDto, "createSubscribeDto must not be null");
        Objects.requireNonNull(application, "application must not be null");
        if (createSubscribeDto.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        SubscriptionPlan newPlan = SubscriptionPlan.builder()
                .planName(createSubscribeDto.getSubscribeName())
                .duration(createSubscribeDto.getDuration())
                .durationUnit(createSubscribeDto.getDurationUnit())
                .price(createSubscribeDto.getPrice())
                .discount(createSubscribeDto.getDiscount())
                .discountType(createSubscribeDto.getDiscountType())
                .application(application)
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

        checkUserAuthorization(subscriptionPlan.getApplication().getUser().getId());

        subscriptionPlan.update(updateSubscribeDto.getSubscribeName(), updateSubscribeDto.getDuration(),
                updateSubscribeDto.getDurationUnit(), updateSubscribeDto.getPrice(), updateSubscribeDto.getDiscount(),
                updateSubscribeDto.getDiscountType());
    }


    /**
     * 구독 Plan 삭제
     *
     * @param planId 삭제할 Plan ID
     */
    @Transactional
    public void deleteSubscribePlan(Long planId) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription plan ID: " + planId));

        checkUserAuthorization(subscriptionPlan.getApplication().getUser().getId());

        if (existsActiveCustomer(planId)) {
            log.error("해당 구독 Plan에 활성화된 구독이 존재합니다.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 구독 Plan에 활성화된 구독이 존재합니다.");
        }

        subscriptionPlanRepository.deleteById(planId);
    }

    private boolean existsActiveCustomer(Long planId) {
        return subscriptionRepository.findAllBySubscriptionPlanId(planId).stream()
                .anyMatch(subscription -> subscription.getStatus().equals(SubscriptionStatus.ACTIVE));
    }

    /**
     * 구독 Plan 조회
     */
    public SubscriptionPlan getSubscribePlan(Long id) {
        return subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscription plan ID: " + id));
    }

    /**
     * 구독 Plan 전체 조회 By Application
     */
    public List<SubscriptionPlan> getSubscriptionPlanByApplicationId(Long applicationId) {
        return subscriptionPlanRepository.findAllByApplicationId(applicationId);
    }

    /**
     * 구독 Plan 전체 조회
     */
    public List<SubscriptionPlan> getAllSubscribePlan() {
        return subscriptionPlanRepository.findAll();
    }

    /**
     * 나의 구독 Plan 조회
     *
     *
     */
//    public List<SubscriptionPlan> getMySubscribePlan(User user, Application application) {
//        Application application = applicationRepository.findByUserId(user.getId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자의 Application이 존재하지 않습니다.: " + user.getId()));
//        return application.getSubscriptionPlans();
//    }


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
            throw new AccessDeniedException("access denied");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            if (!userIdContainsPlan.equals(customUserDetails.toUser().getId())) {
                throw new AccessDeniedException("access denied");
            }
        } else {
            throw new AccessDeniedException("access denied");
        }
    }


}
