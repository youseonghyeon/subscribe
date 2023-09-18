package com.example.subscribify.controller.template;

import com.example.subscribify.entity.Application;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.repository.ApplicationRepository;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/template")
@RequiredArgsConstructor
public class SubscriptionProcessor {

    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    @GetMapping("/subscriptions/{applicationId}")
    public String subscriptionListForm(@PathVariable Long applicationId, Model model) {
        Application application = applicationRepository.findByIdWithSubscriptionPlans(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));
        List<SubscriptionPlan> subscriptionPlans = application.getSubscriptionPlans();
        model.addAttribute("subscriptionPlans", subscriptionPlans);
        return "api/plan-list";
    }

    @GetMapping("/subscriptions/purchase")
    public String purchaseSubscriptionForm(Model model) {
        return "api/purchase";
    }

    @GetMapping("/success")
    public String activateSubscriptionForm() {
        return "api/activate";
    }
}
