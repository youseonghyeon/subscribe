package com.example.subscribify.controller;

import com.example.subscribify.entity.*;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.repository.SubscriptionRepository;
import com.example.subscribify.util.SecurityTestUtils;
import com.example.subscribify.util.SetupTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Import(SetupTestUtils.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @Autowired
    private SetupTestUtils setupTestUtils;

    @Test
    @WithMockUser
    @DisplayName("구독 등록 폼 테스트")
    void enrollSubscriptionForm() throws Exception {
        Application application = setupTestUtils.createApplication(setupTestUtils.createUser());
        mockMvc.perform(get("/subscription/enroll/" + application.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("subscription/enroll"));
    }

    @Test
    @WithMockUser
    @DisplayName("구독 등록 테스트")
    void enrollSubscription() throws Exception {
        Application application = setupTestUtils.createApplication(setupTestUtils.createUser());
        mockMvc.perform(post("/subscription/enroll")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("applicationId", String.valueOf(application.getId()))
                        .param("subscribeName", "테스트 구독 상품")
                        .param("duration", "1")
                        .param("durationUnit", "MONTH")
                        .param("price", "49000")
                        .param("discount", "0")
                        .param("discountType", "NONE")
                        .param("discountedPrice", "49000")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/subscription/*"));
    }

    @Test
    @WithMockUser
    @DisplayName("구독 Plan 상세 폼 테스트")
    void subscriptionDetailForm() throws Exception {
        Application application = setupTestUtils.createApplication(setupTestUtils.createUser());
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        mockMvc.perform(get("/subscription/" + subscriptionPlan.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("subscription/detail"));
    }

    @Test
    @WithMockUser
    @DisplayName("구독 Plan 수정 폼 테스트")
    void subscriptionUpdateForm() throws Exception {
        Application application = setupTestUtils.createApplication(setupTestUtils.createUser());
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        mockMvc.perform(get("/subscription/update/" + subscriptionPlan.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("subscription/update"));

    }

    @Test
    @DisplayName("구독 Plan 수정 테스트")
    void subscriptionUpdate() throws Exception {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        SecurityTestUtils.mockLogin(user);

        //when
        mockMvc.perform(post("/subscription/update/" + subscriptionPlan.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", String.valueOf(subscriptionPlan.getId()))
                        .param("subscribeName", "테스트 수정 상품")
                        .param("duration", "10")
                        .param("durationUnit", "YEAR")
                        .param("price", "90000")
                        .param("discount", "0")
                        .param("discountType", "NONE")
                        .param("discountedPrice", "90000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subscription/" + subscriptionPlan.getId()));

        //then
        SubscriptionPlan updatedSubscriptionPlan = subscriptionPlanRepository.findById(subscriptionPlan.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid subscriptionPlan ID"));
        assertAll("구독 Plan 수정 테스트",
                () -> assertEquals("테스트 수정 상품", updatedSubscriptionPlan.getPlanName()),
                () -> assertEquals(10, updatedSubscriptionPlan.getDuration()),
                () -> assertEquals(DurationUnit.YEAR, updatedSubscriptionPlan.getDurationUnit()),
                () -> assertEquals(90000L, updatedSubscriptionPlan.getPrice()),
                () -> assertEquals(0D, updatedSubscriptionPlan.getDiscount()),
                () -> assertEquals(DiscountUnit.NONE, updatedSubscriptionPlan.getDiscountType()),
                () -> assertEquals(90000L, updatedSubscriptionPlan.getDiscountedPrice()));
    }

    @Test
    @DisplayName("구독 Plan 삭제 성공 테스트 - 현재 활성화된 customer가 없음")
    void subscriptionDeleteSuccessCase() throws Exception {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        Customer customer = setupTestUtils.createCustomer(application);
        Subscription subscription = setupTestUtils.createSubscription(subscriptionPlan, customer);
        SecurityTestUtils.mockLogin(user);

        //when
        mockMvc.perform(post("/subscription/delete/" + subscriptionPlan.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        //then
        boolean existSubscriptionPlans = subscriptionPlanRepository.findById(subscriptionPlan.getId()).isPresent();
        boolean existSubscription = subscriptionRepository.findById(subscription.getId()).isPresent();
        assertFalse(existSubscriptionPlans);
        assertTrue(existSubscription);

    }

    @Test
    @DisplayName("구독 삭제 실패 테스트 - 현재 활성화된 customer가 있음")
    void subscriptionDeleteFailCase() throws Exception {
        //given
        User user = setupTestUtils.createUser();
        Application application = setupTestUtils.createApplication(user);
        SubscriptionPlan subscriptionPlan = setupTestUtils.createSubscriptionPlan(application);
        Customer customer = setupTestUtils.createCustomer(application);
        Subscription subscription = setupTestUtils.createSubscription(subscriptionPlan, customer);
        SecurityTestUtils.mockLogin(user);

        //when then
        subscription.activate();
        mockMvc.perform(post("/subscription/delete/" + subscriptionPlan.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is4xxClientError());

        //then
        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscription.getId());
        assertTrue(optionalSubscription.isPresent());
    }

    @Test
    void apiDocs() {
    }
}
