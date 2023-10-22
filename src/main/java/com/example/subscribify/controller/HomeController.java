package com.example.subscribify.controller;

import com.example.subscribify.domain.AuthUser;
import com.example.subscribify.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(@AuthUser User user) {
        log.info("user={}", user);
        return "index";
    }
}
