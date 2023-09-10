package com.example.subscribify.controller;

import com.example.subscribify.domain.SessionUser;
import com.example.subscribify.dto.CreateSubscribeDto;
import com.example.subscribify.dto.UpdateSubscribeDto;
import com.example.subscribify.entity.DiscountUnit;
import com.example.subscribify.entity.DurationUnit;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.entity.User;
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

@Slf4j
@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionPlanService subscriptionPlanService;


    /**
     * 구독 등록 폼 페이지를 반환합니다.
     *
     * @param model Model 객체
     * @return 구독 등록 폼 페이지 경로
     */
    @GetMapping("/subscription/enroll")
    public String enrollSubscriptionForm(Model model) {
        // 테스트를 위한 더미 데이터
        model.addAttribute("subscribe", mockSubscription());

//        model.addAttribute("subscribe", new CreateSubscribeDto());
        return "subscription/enroll";
    }

    /**
     * 테스트를 위해 더미 구독 데이터를 생성합니다.
     *
     * @return 생성된 더미 구독 DTO
     */
    private static CreateSubscribeDto mockSubscription() {
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return new CreateSubscribeDto(
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
     * @param user               세션에 있는 사용자 정보
     * @return 구독 상세 페이지 경로
     */
    @PostMapping("/subscription/enroll")
    public String enrollSubscription(@ModelAttribute CreateSubscribeDto createSubscribeDto, @SessionUser User user) {
        log.info("createSubscribeDto={}", createSubscribeDto);
        Long planId = subscriptionPlanService.createSubscribePlan(createSubscribeDto, user);
        return "redirect:/subscription/" + planId;
    }

    /**
     * 특정 구독의 상세 페이지를 반환합니다.
     *
     * @param model  Model 객체
     * @param planId 상세 정보를 조회할 구독의 ID
     * @return 구독 상세 페이지 경로
     */
    @GetMapping("/subscription/{planId}")
    public String subscriptionDetail(Model model, @PathVariable Long planId) {
        SubscriptionPlan subscribePlan = subscriptionPlanService.getSubscribePlan(planId);
        model.addAttribute("plan", subscribePlan);
        return "subscription/detail";
    }

    /**
     * 모든 구독 리스트 페이지를 반환합니다.
     *
     * @param model Model 객체
     * @return 구독 리스트 페이지 경로
     */
    @GetMapping("subscription")
    public String subscriptionList(Model model) {
        model.addAttribute("plans", subscriptionPlanService.getAllSubscribePlan());
        return "subscription/list";
    }

    /**
     * 현재 사용자의 구독 리스트 페이지를 반환합니다.
     *
     * @param model Model 객체
     * @param user  세션에 있는 사용자 정보
     * @return 사용자의 구독 리스트 페이지 경로
     */
    @GetMapping("subscription/manage")
    public String MySubscriptionList(Model model, @SessionUser User user) {
        model.addAttribute("subscriptionPlans", subscriptionPlanService.getMySubscribePlan(user));
        return "subscription/list";
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
     * 특정 구독 정보를 업데이트합니다.
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

    @GetMapping("docs/api")
    public String apiDocs() {
        return "subscription/api-docs";
    }


}
