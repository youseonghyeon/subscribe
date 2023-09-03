package com.example.subscribify.entity;

public enum PaymentStatus {
    PENDING,        // 결제 대기 중
    COMPLETED,      // 결제 완료
    CANCELED,       // 결제 취소
    FAILED,         // 결제 실패
    REFUNDED,       // 환불 완료
    PARTIALLY_REFUNDED // 부분 환불 완료
}
