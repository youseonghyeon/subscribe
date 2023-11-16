package com.example.subscribify.service.application;

import com.example.subscribify.config.security.ApiKeyGenerator;
import com.example.subscribify.dto.UpdateApplicationDto;
import com.example.subscribify.dto.controller.CreateApplicationDto;
import com.example.subscribify.entity.Application;
import com.example.subscribify.entity.Subscription;
import com.example.subscribify.entity.User;
import com.example.subscribify.exception.ApplicationNotFoundException;
import com.example.subscribify.repository.ApplicationRepository;
import com.example.subscribify.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final SubscriptionRepository subscriptionRepository;


    @Transactional
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

    @Transactional
    public Application updateKeys(Long applicationId) {
        Application application = findApplicationByIdWithAuth(applicationId);
        String apiKey = ApiKeyGenerator.generateApiKey(32);
        String secretKey = ApiKeyGenerator.generateApiKey(32);

        return application.updateApiKeys(apiKey, secretKey);
    }

    @Transactional
    public void updateOptions(Application application, UpdateApplicationDto updateApplicationDto) {
        application.updateOptions(updateApplicationDto.getDuplicatePaymentOption());
    }

    @Transactional
    public boolean deleteApplication(Long applicationId, LocalDateTime today) {
        Application findApplication = findApplicationByIdWithAuth(applicationId);
        List<Subscription> activeSubscriptions = subscriptionRepository.findActiveSubscriptionsCountByApplicationId(applicationId, today);
        if (activeSubscriptions.isEmpty()) {
            // TODO soft delete 로 변경해야 함. 이후 cascade 설정도 제거해야 함.
            applicationRepository.delete(findApplication);
            return true;
        } else {
            return false;
        }
    }



    public Application findApplicationByIdWithAuth(Long applicationId) {
        Application findApplication = findApplicationById(applicationId);
        findApplication.authCheck();
        return findApplication;
    }
    private Application findApplicationById(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(applicationId));
    }

    public Application findApplicationWithSubscriptionsAndAuth(Long applicationId) {
        // eager loading
        Application findApplication = applicationRepository.findByIdWithSubscriptionPlans(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException(applicationId));
        findApplication.authCheck();
        return findApplication;
    }

    public Application findApplicationByApiKey(String apiKey) {
        return applicationRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new ApplicationNotFoundException(null));
    }

    public List<Application> findApplicationsByUserId(Long userId) {
        return applicationRepository.findByUserId(userId);
    }


}
