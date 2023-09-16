package com.example.subscribify.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "applications")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String apiKey;
    private String secretKey;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "application")
    private List<SubscriptionPlan> subscriptionPlans;

    public void updateKeys(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }
}
