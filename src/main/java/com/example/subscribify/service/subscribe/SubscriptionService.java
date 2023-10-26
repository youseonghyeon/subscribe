package com.example.subscribify.service.subscribe;

import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.*;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.repository.SubscriptionRepository;
import com.example.subscribify.service.customer.CustomerService;
import com.example.subscribify.service.subscribe.options.AllowDuplicationStrategy;
import com.example.subscribify.service.subscribe.options.DisallowDuplicationStrategy;
import com.example.subscribify.service.subscribe.options.SubscriptionStrategy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.subscribify.entity.DuplicatePaymentOption.*;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final CustomerService customerService;
    private final Map<DuplicatePaymentOption, SubscriptionStrategy> strategies;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, SubscriptionPlanRepository subscriptionPlanRepository, CustomerService customerService) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.customerService = customerService;
        this.strategies = new HashMap<>();
        strategies.put(DISALLOW_DUPLICATION, new DisallowDuplicationStrategy());
        strategies.put(ALLOW_DUPLICATION, new AllowDuplicationStrategy());
    }

    @PersistenceContext
    private EntityManager entityManager;

    /**
     *
     * @param customerId 구매자 id (String)
     * @param planId 구매할 plan id (Long)
     * @param authorization API Key (String)
     * @param option 중복 결제 옵션 (DuplicatePaymentOption)
     * @return 구독 ID (Long)
     */
    @Transactional
    public EnrollSubscriptionServiceResponse enrollSubscribe(
            String customerId,
            Long planId,
            String authorization,
            DuplicatePaymentOption option) {

        // 구독 Plan을 가져옴
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalStateException("Invalid subscription plan ID: " + planId));
        Application application = subscriptionPlan.getApplication();

        // API Key를 확인
        if (!authorization.equals(application.getApiKey())) {
            return new EnrollSubscriptionServiceResponse("Invalid Authorization");
        }

        Customer customer = customerService.getOrCreateCustomer(customerId, application.getId());

        // 중복 구독이 가능/ 불가능
        SubscriptionStrategy strategy = strategies.get(option);

        // 등록
        Subscription newSubscription = strategy.apply(customer, subscriptionPlan);

        subscriptionRepository.save(newSubscription);
        return new EnrollSubscriptionServiceResponse(newSubscription.getId());
    }

    @Transactional
    public void activateSubscribe(Long subscriptionId, String authorization) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalStateException("Invalid customer subscription ID: " + subscriptionId));
        String findAuth = subscription.getSubscriptionPlan().getApplication().getApiKey();
        if (findAuth == null || !findAuth.equals(authorization)) {
            // TODO 예외 처리 및 에러 메시지 전송 처리를 해야 함
        } else {
            subscription.activate();
        }
    }


    /**
     * 구독 서비스 취소, 결제는 별도의 서비스로 분리
     * 취소의 경우 환불에 대한 처리가 필요함.
     * 취소는 flag 처리로 하고, 결제일이 도래한 경우 flag에 따라서 결제 처리를 하지 않음.
     */
    @Transactional
    public void cancelSubscribe(Long customerSubscriptionId) {
        Subscription subscription = subscriptionRepository.findById(customerSubscriptionId)
                .orElseThrow(() -> new IllegalStateException("Invalid customer subscription ID: " + customerSubscriptionId));
        subscription.cancel();
    }

    public List<Subscription> getSubscriptions(Long planId) {
        return subscriptionRepository.findAllBySubscriptionPlanId(planId);
    }

    public List<Subscription> getSubscriptionsWithCustomer(Long planId) {
        return subscriptionRepository.findAllWithCustomerBySubscriptionPlanId(planId);
    }

    /**
     * 만기가 되는 구독 서비스를 찾아서 만료 처리
     * 대용량 처리이므로 entityManger를 사용하여 flush, clear 처리
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
}
