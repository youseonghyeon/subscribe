package com.example.subscribify.controller;

import com.example.subscribify.dto.CreateSubscribeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    @GetMapping("/subscription/enroll")
    public String enrollSubscriptionForm(Model model) {
        model.addAttribute("subscribe",new CreateSubscribeDto());
        return "subscription/enroll";
    }

    @PostMapping
    public void createSubscription() {

    }


}
