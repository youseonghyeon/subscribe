package com.example.subscribify.entity;

public enum SubscriptionStatus {

    ACTIVE,       // 구독이 활성화된 상태
    INACTIVE,     // 구독이 비활성화된 상태
    PENDING,      // 구독 활성화 대기 중인 상태
    CANCELED,     // 구독이 취소된 상태
    EXPIRED,      // 구독이 만료된 상태
    SUSPENDED,    // 구독이 일시 중지된 상태
    RENEWING      // 구독이 갱신 중인 상태
}
