package com.example.subscribify.service.payment;

import com.example.subscribify.dto.PaymentDto;
import com.example.subscribify.entity.Payment;
import com.example.subscribify.entity.PaymentStatus;
import com.example.subscribify.repository.PaymentRepository;
import com.example.subscribify.service.subscribe.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;

    /**
     * 결제 서비스
     */
    @Transactional
    public void completePayment(Long subscriptionId, PaymentDto paymentDto) {
        Payment payment = Payment.builder()
                .transactionId(paymentDto.getTransactionId())
                .userId(paymentDto.getUserId())
                .productId(paymentDto.getProductId())
                .amount(paymentDto.getAmount())
                .status(paymentDto.getStatus())
                .build();
        paymentRepository.save(payment);
        if (PaymentStatus.COMPLETED.equals(payment.getStatus())) {
            subscriptionService.activateSubscribe(subscriptionId);
        }
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
