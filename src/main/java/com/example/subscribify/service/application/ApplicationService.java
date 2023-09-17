package com.example.subscribify.service.application;

import com.example.subscribify.config.security.ApiKeyGenerator;
import com.example.subscribify.dto.controller.CreateApplicationDto;
import com.example.subscribify.entity.Application;
import com.example.subscribify.entity.Customer;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.ApplicationRepository;
import com.example.subscribify.repository.CustomerRepository;
import com.example.subscribify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Long createApplication(CreateApplicationDto createApplicationDto, User user) {
        Application application = Application.builder()
                .name(createApplicationDto.getName())
                .apiKey(ApiKeyGenerator.generateApiKey(32))
                .secretKey(ApiKeyGenerator.generateApiKey(32))
                .user(user)
                .build();
        return applicationRepository.save(application).getId();
    }

    public List<Application> getMyApplications(Long userId) {
        return applicationRepository.findByUserId(userId);
    }


    @Transactional
    public Application updateKeys(Long applicationId, Long sessionUserId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));

        if (!application.getUser().getId().equals(sessionUserId)) {
            throw new AccessDeniedException("Access is denied");
        }

        String apiKey = ApiKeyGenerator.generateApiKey(32);
        String secretKey = ApiKeyGenerator.generateApiKey(32);
        application.updateKeys(apiKey, secretKey);
        return application;
    }

}
