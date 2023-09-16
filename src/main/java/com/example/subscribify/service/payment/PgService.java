package com.example.subscribify.service.payment;

import com.example.subscribify.dto.controller.PaymentRequest;
import com.example.subscribify.dto.controller.PaymentResponse;
import com.example.subscribify.dto.RefundRequest;
import com.example.subscribify.dto.RefundResponse;
import com.example.subscribify.entity.PaymentStatus;

public interface PgService {

    // 결제 요청 메서드
    PaymentResponse processPayment(PaymentRequest paymentRequest);

    // 결제 상태 조회 메서드
    PaymentStatus checkPaymentStatus(String transactionId);

    // 결제 취소 및 환불 메서드
    RefundResponse processRefund(RefundRequest refundRequest);

}
