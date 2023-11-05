package com.example.subscribify.service.subscribe;

import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.*;
import com.example.subscribify.exception.SubscriptionPlanNotFoundException;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.repository.SubscriptionRepository;
import com.example.subscribify.service.customer.CustomerService;
import com.example.subscribify.service.subscribe.options.AllowDuplicationStrategy;
import com.example.subscribify.service.subscribe.options.DisallowDuplicationStrategy;
import com.example.subscribify.service.subscribe.options.SubscriptionStrategy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final CustomerService customerService;
    private final AllowDuplicationStrategy allowDuplicationStrategy;
    private final DisallowDuplicationStrategy disallowDuplicationStrategy;

    @PersistenceContext
    private EntityManager entityManager;


    /**
     * 구독 서비스 등록
     *
     * @param customerId    고객 ID
     * @param planId        구독 상품 ID
     * @param authorization 고객사 API Key
     * @return 등록된 구독 서비스 ID
     */
    @Transactional
    public EnrollSubscriptionServiceResponse enrollInSubscription(String customerId, Long planId, String authorization) {
        SubscriptionPlan subscriptionPlan = validateAndGetSubscriptionPlan(planId);
        verifyAuthorization(subscriptionPlan, authorization);

        Customer customer = customerService.getOrCreateCustomer(customerId, subscriptionPlan.getApplication().getId());
        SubscriptionStrategy strategy = determineSubscriptionStrategy(subscriptionPlan.getApplication());

        Subscription newSubscription = createAndSaveSubscription(customer, subscriptionPlan, strategy);
        return new EnrollSubscriptionServiceResponse(newSubscription.getId());
    }

    /**
     * 구독 서비스 활성화
     *
     * @param subscriptionId
     * @param authorization
     */
    @Transactional
    public void activateSubscribe(Long subscriptionId, String authorization) {
        Subscription subscription = validateAndGetSubscription(subscriptionId);
        String findAuth = subscription.getSubscriptionPlan().getApplication().getApiKey();
        if (findAuth == null || !findAuth.equals(authorization)) {
            // TODO 예외 처리 및 에러 메시지 전송 처리를 해야 함
        } else {
            subscription.activate();
        }
    }

    /**
     * 구독 서비스 취소 (status 변경 으로 처리)
     *
     * @param customerSubscriptionId
     */
    @Transactional
    public void cancelSubscribe(Long customerSubscriptionId) {
        Subscription subscription = validateAndGetSubscription(customerSubscriptionId);
        subscription.cancel();
    }

    /**
     * 만기가 되는 구독 서비스를 찾아서 만료 처리
     * 대용량 처리 이므로 flush, clear를 통해 영속성 컨텍스트를 비워줌
     *
     * @param currentDateTime
     * @return
     */
    @Transactional
    public Integer expireSubscriptions(LocalDateTime currentDateTime) {
        List<Subscription> expiredSubscriptions =
                subscriptionRepository.findAllByStatusAndEndDateBefore(SubscriptionStatus.ACTIVE, currentDateTime);

        expiredSubscriptions.forEach(Subscription::expire);
        entityManager.flush();
        entityManager.clear();
        return expiredSubscriptions.size();
    }

    @Transactional
    public Integer deleteUnpaidSubscriptions(LocalDateTime localDate) {
        List<Subscription> unpaidSubscriptions =
                subscriptionRepository.findAllByStatusAndCreatedAtBefore(SubscriptionStatus.PENDING, localDate);
        subscriptionRepository.deleteAll(unpaidSubscriptions);
        entityManager.flush();
        entityManager.clear();
        return unpaidSubscriptions.size();
    }


    public Subscription getSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new NoSuchElementException("Invalid subscription ID: " + subscriptionId));
    }

    public List<Subscription> getSubscriptionsWithCustomer(Long planId) {
        return subscriptionRepository.findAllWithCustomerBySubscriptionPlanId(planId);
    }

    public List<Subscription> getReachedExpireDaySubscriptions(LocalDate today) {
        LocalDateTime start = today.plusDays(1).atStartOfDay();
        LocalDateTime end = today.plusDays(2).atStartOfDay();
        return subscriptionRepository.findAllByStatusAndEndDateBetween(SubscriptionStatus.ACTIVE, start, end);
    }

    private SubscriptionPlan validateAndGetSubscriptionPlan(Long planId) {
        return subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new SubscriptionPlanNotFoundException(planId));
    }

    private void verifyAuthorization(SubscriptionPlan subscriptionPlan, String authorization) {
        subscriptionPlan.getApplication().apiKeyCheck(authorization);
    }

    private SubscriptionStrategy determineSubscriptionStrategy(Application application) {
        return switch (application.getDuplicatePaymentOption()) {
            case ALLOW_DUPLICATION -> allowDuplicationStrategy;
            case DISALLOW_DUPLICATION -> disallowDuplicationStrategy;
        };
    }

    private Subscription createAndSaveSubscription(Customer customer, SubscriptionPlan subscriptionPlan, SubscriptionStrategy strategy) {
        Subscription subscription = strategy.apply(customer, subscriptionPlan);
        return subscriptionRepository.save(subscription);
    }

    private Subscription validateAndGetSubscription(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new NoSuchElementException("Invalid customer subscription ID: " + subscriptionId));
    }

}
