package com.example.subscribify.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SecurityController {

    @GetMapping("/isAuthenticated")
    public String isAuthenticated(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return authentication.getName();
    }
}
