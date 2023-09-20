package com.example.subscribify.controller;

import com.example.subscribify.dto.controller.CreateSubscribeDto;
import com.example.subscribify.dto.controller.UpdateSubscribeDto;
import com.example.subscribify.entity.*;
import com.example.subscribify.repository.ApplicationRepository;
import com.example.subscribify.service.subscribe.SubscriptionService;
import com.example.subscribify.service.subscriptionplan.SubscriptionPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionPlanService subscriptionPlanService;
    private final SubscriptionService subscriptionService;
    private final ApplicationRepository applicationRepository;


    /**
     * 구독 등록 폼 페이지를 반환합니다.
     *
     * @param model Model 객체
     * @return 구독 등록 폼 페이지 경로
     */
    @GetMapping("/subscription/enroll/{applicationId}")
    public String enrollSubscriptionForm(Model model, @PathVariable Long applicationId) {
        // 테스트를 위한 더미 데이터
        model.addAttribute("subscribe", mockSubscription(applicationId));

//        model.addAttribute("subscribe", new CreateSubscribeDto());
        return "subscription/enroll";
    }

    /**
     * 테스트를 위해 더미 구독 데이터를 생성합니다.
     *
     * @return 생성된 더미 구독 DTO
     */
    private static CreateSubscribeDto mockSubscription(Long applicationId) {
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return new CreateSubscribeDto(applicationId,
                "테스트 구독 상품" + nowTime,
                1,
                DurationUnit.MONTH,
                49000L,
                0D,
                DiscountUnit.NONE,
                49000L
        );
    }

    /**
     * 새로운 구독을 등록합니다.
     *
     * @param createSubscribeDto 생성될 구독 정보 DTO
     * @return 구독 상세 페이지 경로
     */
    @PostMapping("/subscription/enroll")
    public String enrollSubscription(@ModelAttribute CreateSubscribeDto createSubscribeDto) {
        Application application = applicationRepository.findById(createSubscribeDto.getApplicationId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));
        Long planId = subscriptionPlanService.createSubscribePlan(createSubscribeDto, application);
        return "redirect:/subscription/" + planId;
    }

    /**
     * 특정 구독의 상세 페이지를 반환합니다.
     *
     * @param model  Model 객체
     * @param subscriptionPlanId 상세 정보를 조회할 구독의 ID
     * @return 구독 상세 페이지 경로
     */
    @GetMapping("/subscription/{subscriptionPlanId}")
    public String subscriptionDetail(Model model, @PathVariable Long subscriptionPlanId) {
        SubscriptionPlan subscribePlan = subscriptionPlanService.getSubscribePlan(subscriptionPlanId);
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsWithCustomer(subscriptionPlanId);
        Long applicationId = subscribePlan.getApplication().getId();
        model.addAttribute("subscriptionPlan", subscribePlan);
        model.addAttribute("subscriptions", subscriptions);
        model.addAttribute("applicationId", applicationId);
        return "subscription/detail";
    }


    /**
     * 구독 수정 폼 페이지를 반환합니다.
     *
     * @param model  Model 객체
     * @param planId 수정할 구독의 ID
     * @return 구독 수정 폼 페이지 경로
     */
    @GetMapping("subscription/update/{planId}")
    public String subscriptionUpdateForm(Model model, @PathVariable Long planId) {
        SubscriptionPlan subscriptionPlan = subscriptionPlanService.getSubscribePlan(planId);
        UpdateSubscribeDto updateSubscribeDto = new UpdateSubscribeDto(subscriptionPlan);
        model.addAttribute("updateSubscribeDto", updateSubscribeDto);
        return "subscription/update";
    }

    /**
     * 구독 정보를 업데이트합니다.
     *
     * @param updateSubscribeDto 수정될 구독 정보 DTO
     * @param planId             수정할 구독의 ID
     * @return 업데이트 된 구독의 상세 페이지 경로
     */
    @PostMapping("subscription/update/{planId}")
    public String subscriptionUpdate(@ModelAttribute UpdateSubscribeDto updateSubscribeDto, @PathVariable Long planId) {
        log.info("updateSubscribeDto={}", updateSubscribeDto);
        subscriptionPlanService.updateSubscribePlan(planId, updateSubscribeDto);
        return "redirect:/subscription/" + planId;
    }

    /**
     * 구독을 삭제합니다.
     *
     * @return
     */
    @PostMapping("subscription/delete/{planId}")
    public String subscriptionDelete(@PathVariable Long planId) {
        subscriptionPlanService.deleteSubscribePlan(planId);
        return "redirect:/";
    }



}
