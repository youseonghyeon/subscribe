package com.example.subscribify.service.application;

import com.example.subscribify.config.security.ApiKeyGenerator;
import com.example.subscribify.dto.ApplicationEnrollDto;
import com.example.subscribify.entity.Application;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.ApplicationRepository;
import com.example.subscribify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long enrollApplication(ApplicationEnrollDto applicationEnrollDto) {
        User user = userRepository.findById(applicationEnrollDto.getUserid())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Application application = Application.builder()
                .name(applicationEnrollDto.getName())
                .apiKey(ApiKeyGenerator.generateApiKey(32))
                .secretKey(ApiKeyGenerator.generateApiKey(32))
                .user(user)
                .build();
        return applicationRepository.save(application).getId();
    }

}
