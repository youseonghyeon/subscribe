package com.example.subscribify.util;

import com.example.subscribify.config.security.CustomUserDetails;
import com.example.subscribify.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityTestUtils {

    public static void mockLogin(User user) {
        mockLogin(new CustomUserDetails(user));
    }

    public static void mockLogin(CustomUserDetails customUserDetails) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
