package com.example.subscribify.config;

import com.example.subscribify.config.security.SessionUserArgumentResolver;
import com.example.subscribify.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationService applicationService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SessionUserArgumentResolver());
        resolvers.add(new AuthApplicationArgumentResolver(applicationService));
    }
}
