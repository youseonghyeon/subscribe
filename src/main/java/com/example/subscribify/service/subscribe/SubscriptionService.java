package com.example.subscribify.service.subscribe;

import com.example.subscribify.dto.OptionResult;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.entity.*;
import com.example.subscribify.exception.SubscriptionPlanNotFoundException;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.repository.SubscriptionRepository;
import com.example.subscribify.service.customer.CustomerService;
import com.example.subscribify.service.payment.discountstrategy.DiscountPolicy;
import com.example.subscribify.service.payment.discountstrategy.DiscountPolicyFactory;
import com.example.subscribify.service.subscribe.options.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.subscribify.entity.DuplicatePaymentOption.DISALLOW_DUPLICATION;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final CustomerService customerService;

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

        Subscription newSubscription = createAndSaveSubscription(customer, subscriptionPlan);
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

    /**
     * 구독 서비스 등록
     *
     * @param customer
     * @param subscriptionPlan
     * @return
     */
    private Subscription createAndSaveSubscription(Customer customer, SubscriptionPlan subscriptionPlan) {
        Application application = subscriptionPlan.getApplication();
        // TODO Lazy Loading 을 위한 임시 코드
        // TODO Eager Loading 을 고려중
        List<Subscription> subscriptions = customer.getSubscriptions();

        OptionComponent subscriptionStrategy = determineSubscriptionStrategy(application);
        // TODO 현재는 optionResult의 결과값이 없음. 추후 추가 예정
        OptionResult optionResult = subscriptionStrategy.apply(customer, subscriptionPlan);

        long discountedPrice = getDiscountedPrice(subscriptionPlan);

        Subscription subscription = createSubscription(customer, subscriptionPlan, discountedPrice, optionResult);
        return subscriptionRepository.save(subscription);
    }

    private static long getDiscountedPrice(SubscriptionPlan subscriptionPlan) {
        DiscountPolicy discountPolicy = DiscountPolicyFactory.createPolicy(subscriptionPlan.getDiscountType());
        return discountPolicy.calculateDiscountedAmount(subscriptionPlan.getPrice(), subscriptionPlan.getDiscount());
    }

    private static Subscription createSubscription(Customer customer, SubscriptionPlan subscriptionPlan, long discountedPrice, OptionResult optionResult) {
        // 3. 객체 생성
        // optionResult 데이터 사용 예정
        return Subscription.builder()
                .subscribeName(subscriptionPlan.getPlanName())
                .startDate(null)
                .endDate(null)
                .durationMonth(subscriptionPlan.getDuration())
                .status(SubscriptionStatus.PENDING)
                .price(subscriptionPlan.getPrice())
                .discountedPrice(discountedPrice)
                .customer(customer)
                .subscriptionPlan(subscriptionPlan)
                .build();
    }

    private OptionComponent determineSubscriptionStrategy(Application application) {
        OptionComponent component = new ConcreteOptionComponent();
        component = new OptionDecorator(component);
        if (application.getDuplicatePaymentOption().equals(DISALLOW_DUPLICATION)) {
            component = new DisallowDuplicationStrategy(component);
        } else {
            component = new AllowDuplicationStrategy(component);
        }
        // 부가 옵션 처리
        // ...
        return component;
    }


    private Subscription validateAndGetSubscription(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new NoSuchElementException("Invalid customer subscription ID: " + subscriptionId));
    }

}
