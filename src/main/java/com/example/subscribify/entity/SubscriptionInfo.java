package com.example.subscribify.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String productId;
    private String productName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    private Integer price;
    private Double discountRate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SubscribeUser subscribeUser;
}
