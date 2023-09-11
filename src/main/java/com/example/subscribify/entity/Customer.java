package com.example.subscribify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    // 고객 시스템에서 사용하는 유저 아이디
    private String customerId;
    // 어플리케이션 아이디
    private Long applicationId;

    @OneToMany(mappedBy = "customer")
    private List<Subscription> subscriptions;


}
