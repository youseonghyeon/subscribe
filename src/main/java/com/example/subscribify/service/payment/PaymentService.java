package com.example.subscribify.service.payment;

import com.example.subscribify.entity.PaymentStatus;
import com.example.subscribify.entity.Payment;
import com.example.subscribify.entity.Subscription;
import com.example.subscribify.repository.PaymentRepository;
import com.example.subscribify.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 결제 서비스
     */
    @Transactional
    public void payment(Long subscriptionId, Payment payment) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalStateException("Invalid subscription ID: " + subscriptionId));

        assert (payment.getUserId().equals(subscription.getCustomer().getCustomerId()));

        if (PaymentStatus.COMPLETED.equals(payment.getStatus())) {
            subscription.start();
        }

        paymentRepository.save(payment);
    }

    /**
     * 결제 상태 변경 서비스
     */
    public void paymentStatusChange() {
        // 결제 상태 변경
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * 정기 결제 서비스
     */
    public void regularPayment() {
        // 정기 결제
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
