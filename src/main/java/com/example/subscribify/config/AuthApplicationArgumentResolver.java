package com.example.subscribify.config;

import com.example.subscribify.domain.AuthApplication;
import com.example.subscribify.entity.Application;
import com.example.subscribify.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthApplicationArgumentResolver implements HandlerMethodArgumentResolver {

    private final ApplicationService applicationService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthApplication.class) && parameter.getGenericParameterType().equals(Application.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String auth = webRequest.getHeader("Authorization");
        if (auth != null) {
            auth = auth.replace("Bearer ", "").trim();
        }
        return applicationService.findApplicationByApiKey(auth);

    }
}
