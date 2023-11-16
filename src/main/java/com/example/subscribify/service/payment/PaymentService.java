package com.example.subscribify.service.payment;

import com.example.subscribify.entity.DiscountUnit;
import com.example.subscribify.entity.Payment;
import com.example.subscribify.entity.PaymentStatus;
import com.example.subscribify.entity.SubscriptionPlan;
import com.example.subscribify.repository.PaymentRepository;
import com.example.subscribify.repository.SubscriptionPlanRepository;
import com.example.subscribify.service.payment.strategy.DiscountPolicy;
import com.example.subscribify.service.payment.strategy.DiscountPolicyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PgService pgService;

    // TODO 미완. 로직 설계를 더 해야 함
    public void pay(String uerId, Long applicationId, Long productId, Long amount, PaymentStatus status) {
        // 결제 로직 위치


        // 결제 결과 저장 로직 위치
        Payment payment = Payment.builder()
                .transactionId(UUID.randomUUID().toString())
                .userId(uerId)
                .applicationId(applicationId)
                .productId(productId)
                .amount(amount)
                .status(status)
                .build();
        paymentRepository.save(payment);
    }

    public void payToPgApi(String userId, Long planId) {
        SubscriptionPlan subscriptionPlan = getSubscriptionPlan(planId);
        DiscountUnit discountType = subscriptionPlan.getDiscountType();
        DiscountPolicy discountPolicy = DiscountPolicyFactory.create(discountType);
        long totalPrice = discountPolicy.calculateDiscountAmount(subscriptionPlan.getPrice(), subscriptionPlan.getDiscountType(), subscriptionPlan.getDiscount());
//        pgService.processPayment(userId, totalPrice, subscriptionPlan.getPaymentType())

    }


    // 금액 조회
    public Long getPaymentAmount(Long subscriptionPlanId) {
        SubscriptionPlan plan = getSubscriptionPlan(subscriptionPlanId);
        DiscountUnit discountType = plan.getDiscountType();
        DiscountPolicy discountPolicy = DiscountPolicyFactory.create(discountType);
        return discountPolicy.calculateDiscountAmount(plan.getPrice(), plan.getDiscountType(), plan.getDiscount());
    }

    private SubscriptionPlan getSubscriptionPlan(Long subscriptionPlanId) {
        return subscriptionPlanRepository.findById(subscriptionPlanId).orElseThrow(() -> new IllegalArgumentException("Invalid subscriptionPlanId"));
    }


    /**
     * 결제 서비스
     */
//    @Transactional
//    public void completePayment(Long subscriptionId, PaymentServiceRequest paymentServiceRequest) {
//        Payment payment = Payment.builder()
//                .transactionId(paymentServiceRequest.getTransactionId())
//                .userId(paymentServiceRequest.getUserId())
//                .productId(paymentServiceRequest.getProductId())
//                .amount(paymentServiceRequest.getAmount())
//                .status(paymentServiceRequest.getStatus())
//                .build();
//        paymentRepository.save(payment);
//        if (PaymentStatus.COMPLETED.equals(payment.getStatus())) {
//            subscriptionService.activateSubscribe(subscriptionId);
//        }
//    }
    public void paymentStatusChange() {
        // 결제 상태 변경
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void regularPayment() {
        // 정기 결제
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<Payment> getPaymentLog(Long subscriptionPlanId) {
        return paymentRepository.findAllByApplicationId(subscriptionPlanId);
    }

    public Long sumAmountByApplicationId(Long applicationId) {
        return paymentRepository.sumAmountByApplicationId(applicationId);
    }

}
