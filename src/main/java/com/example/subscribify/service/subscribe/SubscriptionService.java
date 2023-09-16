package com.example.subscribify.service.subscribe;

import com.example.subscribify.dto.service.EnrollSubscriptionServiceResponse;
import com.example.subscribify.dto.service.EnrollSubscriptionServiceRequest;
import com.example.subscribify.entity.*;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;

    /**
     * 구독 서비스 구매
     *
     * @param serviceRequest  구매자 정보, 구독 Plan 정보, API Key, Secret Key
     * @return 구독 구매 ID, 단 현재 단계 에서는 구독이 시작 되지 않음 (결제가 되면 구독이 시작됨)
     */
    @Transactional
    public EnrollSubscriptionServiceResponse enrollSubscribe(EnrollSubscriptionServiceRequest serviceRequest) {
        // 구독 Plan을 가져와서
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(serviceRequest.getPlanId())
                .orElseThrow(() -> new IllegalStateException("Invalid subscription plan ID: " + serviceRequest.getPlanId()));
        Application application = subscriptionPlan.getApplication();

        String findApiKey = application.getApiKey();
        String findSecretKey = application.getSecretKey();
        if (!findApiKey.equals(serviceRequest.getApiKey()) || !findSecretKey.equals(serviceRequest.getSecretKey())) {
            return new EnrollSubscriptionServiceResponse("Invalid API Key or Secret Key");
        }

        // plan 정보와 user 정보를 저장
        Subscription subscription = Subscription.builder()
                .subscribeName(subscriptionPlan.getPlanName())
                .status(SubscriptionStatus.PENDING)
                .price(subscriptionPlan.getPrice())
                .durationMonth(subscriptionPlan.getDuration())
                .discountedPrice(subscriptionPlan.getDiscountedPrice())
                .subscriptionPlan(subscriptionPlan)
                .customer(serviceRequest.getCustomer())
                .build();

        subscriptionRepository.save(subscription);

        return new EnrollSubscriptionServiceResponse(subscription.getId());
    }

    @Transactional
    public void activateSubscribe(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalStateException("Invalid customer subscription ID: " + subscriptionId));
        subscription.start();
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
}