package com.example.subscribify.controller;

import com.example.subscribify.dto.CreateSubscribeDto;
import com.example.subscribify.entity.DiscountUnit;
import com.example.subscribify.entity.DurationUnit;
import com.example.subscribify.entity.SubscriptionPlan;
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

    @GetMapping("/subscription/enroll")
    public String enrollSubscriptionForm(Model model) {
        // 테스트를 위한 더미 데이터
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        model.addAttribute("subscribe", new CreateSubscribeDto(
                "테스트 구독 상품" + nowTime,
                1,
                DurationUnit.MONTH,
                49000L,
                0D,
                DiscountUnit.NONE,
                49000L
        ));

//        model.addAttribute("subscribe", new CreateSubscribeDto());
        return "subscription/enroll";
    }

    @PostMapping
    public String enrollSubscription(@ModelAttribute CreateSubscribeDto createSubscribeDto) {
        log.info("createSubscribeDto={}", createSubscribeDto);
        Long planId = subscriptionPlanService.createSubscribePlan(createSubscribeDto);
        return "redirect:/subscription/" + planId;
    }

    @GetMapping("/subscription/{planId}")
    public String subscriptionDetail(Model model, @PathVariable Long planId) {
        SubscriptionPlan subscribePlan = subscriptionPlanService.getSubscribePlan(planId);
        model.addAttribute("plan", subscribePlan);
        return "subscription/detail";
    }

    @GetMapping("/subscription/{planId}/edit")
    public String subscriptionEditForm(Model model, @PathVariable Long planId) {
        SubscriptionPlan subscribePlan = subscriptionPlanService.getSubscribePlan(planId);
        model.addAttribute("plan", subscribePlan);
        return "subscription/edit";
    }

    @GetMapping("subscription")
    public String subscriptionList(Model model) {
        model.addAttribute("plans", subscriptionPlanService.getAllSubscribePlan());
        return "subscription/list";
    }


}
