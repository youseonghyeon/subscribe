package com.example.subscribify.controller;

import com.example.subscribify.domain.SessionUser;
import com.example.subscribify.dto.controller.CreateApplicationDto;
import com.example.subscribify.entity.Application;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.ApplicationRepository;
import com.example.subscribify.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;

    @GetMapping("/applications")
    public String applicationListForm(@SessionUser User user, Model model) {
        List<Application> applications = applicationService.getMyApplications(user.getId());
        model.addAttribute("applications", applications);
        return "application/list";
    }

    @GetMapping("/applications/{applicationId}")
    public String applicationDetailForm(@PathVariable Long applicationId, Model model) {
        Application application = applicationRepository.findByIdWithSubscriptionPlans(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));
        model.addAttribute("app", application);
        model.addAttribute("subscriptionPlans", application.getSubscriptionPlans());
        return "application/detail";
    }

    @GetMapping("/applications/enroll")
    public String enrollApplicationForm(Model model) {
        model.addAttribute("createApplicationDto", new CreateApplicationDto());
        return "application/enroll";
    }

    @PostMapping("/applications/enroll")
    public String enrollApplication(@ModelAttribute CreateApplicationDto createApplicationDto, @SessionUser User user) {
        applicationService.createApplication(createApplicationDto, user);
        return "redirect:/applications";
    }


    @PostMapping("/applications/keys/generate")
    public String generateApiKey(@RequestParam("applicationId") Long applicationId, @SessionUser User user, Model model) {
        Application updatedApplication = applicationService.updateKeys(applicationId, user.getId());
        model.addAttribute("app", updatedApplication);
        return "redirect:/applications/" + applicationId;
    }

}
