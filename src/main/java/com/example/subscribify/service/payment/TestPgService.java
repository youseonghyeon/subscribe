package com.example.subscribify.service.payment;

import com.example.subscribify.dto.RefundRequest;
import com.example.subscribify.dto.RefundResponse;
import com.example.subscribify.dto.controller.PaymentRequest;
import com.example.subscribify.dto.controller.PaymentResponse;
import com.example.subscribify.entity.PaymentStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * PG사와의 통신을 대신하는 테스트용 서비스
 */
@Slf4j
public class TestPgService implements PgService {


    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        log.info("== PG 결제 성공 ==");
        return new PaymentResponse(paymentRequest.getTransactionId(), paymentRequest.getUserId(), paymentRequest.getProductId(), paymentRequest.getAmount(), PaymentStatus.COMPLETED);
    }

    @Override
    public PaymentStatus checkPaymentStatus(String transactionId) {
        return PaymentStatus.COMPLETED;
    }

    @Override
    public RefundResponse processRefund(RefundRequest refundRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
