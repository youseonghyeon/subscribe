package com.example.subscribify.controller;

import com.example.subscribify.domain.SessionUser;
import com.example.subscribify.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DefaultController {

    @GetMapping("/")
    public Object test(@SessionUser User user) {
        log.info("test");
        if (user != null) {
            log.info("user: {}", user.getUsername());
        }
        return "요청 성공";
    }
}
