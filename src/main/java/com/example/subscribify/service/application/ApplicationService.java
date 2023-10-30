package com.example.subscribify.service.application;

import com.example.subscribify.config.security.ApiKeyGenerator;
import com.example.subscribify.dto.UpdateApplicationDto;
import com.example.subscribify.dto.controller.CreateApplicationDto;
import com.example.subscribify.entity.Application;
import com.example.subscribify.entity.User;
import com.example.subscribify.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;


    public Long createApplication(CreateApplicationDto createApplicationDto, User user) {
        Application application = Application.builder()
                .name(createApplicationDto.getName())
                .apiKey(ApiKeyGenerator.generateApiKey(32))
                .secretKey(ApiKeyGenerator.generateApiKey(32))
                .user(user)
                .build();
        applicationRepository.save(application);
        log.info("Application created: {}, by User: {}", application.getName(), user.getUsername());
        return application.getId();
    }

    public Application getApplication(Long applicationId, User userForAuthCheck) {
        Application findApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("Invalid application ID"));
        findApplication.authCheck(userForAuthCheck.getId());
        return findApplication;
    }

    public Application getApplicationWithSubscriptionPlan(Long applicationId, User userForAuthCheck) {
        Application findApplication = applicationRepository.findByIdWithSubscriptionPlans(applicationId)
                .orElseThrow(() -> new NoSuchElementException("Invalid application ID"));
        findApplication.authCheck(userForAuthCheck.getId());
        return findApplication;
    }

    public List<Application> findApplicationsByUserId(Long userId) {
        return applicationRepository.findByUserId(userId);
    }

    @Transactional
    public Application updateKeys(Long applicationId, User user) {
        Application application = this.getApplication(applicationId, user);
        String apiKey = ApiKeyGenerator.generateApiKey(32);
        String secretKey = ApiKeyGenerator.generateApiKey(32);

        return application.updateKeys(apiKey, secretKey);
    }


    /**
     * Updates the options of a given application.
     *
     * @param application          An Application object that should be in a persistent state within the current
     *                             Hibernate Session or JPA Persistence Context. Any changes to this object
     *                             will be automatically persisted upon transaction commit, due to the dirty
     *                             checking feature of JPA/Hibernate.
     * @param updateApplicationDto DTO containing the new options to be updated.
     */

    @Transactional
    public void updateOptions(Application application, UpdateApplicationDto updateApplicationDto) {
        application.updateOptions(updateApplicationDto.getDuplicatePaymentOption());
    }
}
