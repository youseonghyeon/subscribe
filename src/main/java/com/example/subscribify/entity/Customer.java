package com.example.subscribify.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@Table(indexes = {@Index(columnList = "applicationId")})
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    // 고객 시스템에서 사용하는 유저 아이디
    private String customerId;
    private Long applicationId;

    @OneToMany(mappedBy = "customer")
    private List<Subscription> subscriptions;


}
