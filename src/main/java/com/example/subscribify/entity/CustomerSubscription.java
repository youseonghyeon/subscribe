package com.example.subscribify.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerSubscription extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String subscribeName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    private Integer price;
    private Double discountRate;
    @ManyToOne(fetch = FetchType.LAZY)
    private SubscribeUser subscribeUser;

}
