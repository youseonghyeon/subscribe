package com.example.subscribify.config;

import com.example.subscribify.domain.AuthApplication;
import com.example.subscribify.entity.Application;
import com.example.subscribify.repository.ApplicationRepository;
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

    private final ApplicationRepository applicationRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthApplication.class) && parameter.getGenericParameterType().equals(Application.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String auth = webRequest.getHeader("Authorization");
        return applicationRepository.findByApiKey(auth)
                .orElseThrow(() -> new IllegalArgumentException("Invalid API Key"));
        // 어려웠던 부분.
        // api에는 여러 기능들이 있는데 검증로직을 어디에 두어야 할지 고민이었다.
        // argumentResolver를 이용해서 접근 가능한 application을 반환하는 형식으로 변경함
    }
}
