package com.example.subscribify.service.application;

import com.example.subscribify.dto.UpdateApplicationDto;
import com.example.subscribify.dto.controller.CreateApplicationDto;
import com.example.subscribify.entity.*;
import com.example.subscribify.repository.ApplicationRepository;
import com.example.subscribify.util.SecurityTestUtils;
import com.example.subscribify.util.SetupTestUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(SetupTestUtils.class)
@Transactional
class ApplicationServiceTest {

    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    SetupTestUtils setupTestUtils;
    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Application 생성 성공 테스트")
    void createApplicationSuccessCase() {
        //given
        CreateApplicationDto applicationDto = new CreateApplicationDto("icon", "name", "businessName");
        User user = setupTestUtils.createUser();
        //when
        Long applicationId = applicationService.createApplication(applicationDto, user);
        //then
        Optional<Application> application = applicationRepository.findById(applicationId);
        assertTrue(application.isPresent());
        // TODO Icon 추가 예정
//        assertEquals(applicationDto.getIcon(), application.get().getIcon());
        assertEquals(applicationDto.getName(), application.get().getName());
        // TODO BusinessName 추가 예정
//        assertEquals(applicationDto.getBusinessName(), application.get().getBusinessName());
    }

    @Test
    @WithMockUser
    @DisplayName("Application 조회 성공 테스트")
    void getApplicationSuccessCase() {
        User user1 = setupTestUtils.createUser("user1");
        Application application = setupTestUtils.createApplication(user1);
        SecurityTestUtils.mockLogin(user1);
        //when
        applicationService.findApplicationByIdWithAuth(application.getId());

        //then
        assertNotNull(application);
        assertEquals(user1, application.getUser());
    }

    @Test
    @DisplayName("Application 조회 실패 테스트 (인가 실패)")
    void getApplicationAuthCheckFailCase() {
        User user1 = setupTestUtils.createUser("user1");
        Application application = setupTestUtils.createApplication(user1);

        //when (다른 유저의 get 요청)
        User user2 = setupTestUtils.createUser("user2");
        assertThrows(AccessDeniedException.class,
                () -> applicationService.findApplicationByIdWithAuth(application.getId()));
    }

    @Test
    @DisplayName("Application 조회 실패 테스트 (존재하지 않는 Application ID로 조회)")
    void getApplicationNoSuchElementsFailTest() {
        User user1 = setupTestUtils.createUser("user1");
        assertThrows(NoSuchElementException.class,
                () -> applicationService.findApplicationByIdWithAuth(5724658723L));
    }

    @Test
    @DisplayName("Application 조회. SubscriptionPlan Eager 로딩 테스트")
    void shouldEagerlyLoadSubscriptionPlansWithApplication() {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        SecurityTestUtils.mockLogin(user);
        em.flush();
        em.clear();

        //when
        Application findApplication = applicationService.findApplicationWithSubscriptionsAndAuth(application.getId());
        em.flush();
        em.clear();

        //then
        assertEquals(findApplication.getId(), application.getId());
        assertEquals(findApplication.getName(), application.getName());
        assertEquals(1, findApplication.getSubscriptionPlans().size());
        assertEquals(subscriptionPlan.getPlanName(), findApplication.getSubscriptionPlans().get(0).getPlanName());
    }

    @Test
    @WithMockUser
    @DisplayName("api 키 재설정")
    void updateKeysTest() {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        String apiKey = application.getApiKey();
        String secretKey = application.getSecretKey();

        SecurityTestUtils.mockLogin(user);
        //when
        applicationService.updateKeys(application.getId());
        em.flush();
        em.clear();
        //then
        Application findApplication = applicationRepository.findById(application.getId()).get();
        assertNotEquals(apiKey, findApplication.getApiKey());
        assertNotEquals(secretKey, findApplication.getSecretKey());
    }

    @Test
    @DisplayName("option 재설정")
    void modifyOptionTest() {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        UpdateApplicationDto updateApplicationDto = new UpdateApplicationDto(DuplicatePaymentOption.ALLOW_DUPLICATION);
        //when
        applicationService.updateOptions(application, updateApplicationDto);
        //then
    }

    /**
     * TODO 다시 작성해야 합ㄴ디ㅏ.
     */
    @Test
    void deleteApplicationTest() {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        SecurityTestUtils.mockLogin(user);

        Customer customer = setupTestUtils.createCustomer(application);
        Customer customer2 = setupTestUtils.createCustomer(application);

        setupTestUtils.createSubscription(subscriptionPlan, customer2);
        Subscription subscription = setupTestUtils.createSubscription(subscriptionPlan, customer);
        subscription.activate();

        //when
        applicationService.deleteApplication(application.getId(), LocalDateTime.now());
    }

}
