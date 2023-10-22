package com.example.subscribify.entity;

import jakarta.persistence.*;
import lombok.*;

import java.nio.file.AccessDeniedException;
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

    @Enumerated(EnumType.STRING)
    private DuplicatePaymentOption duplicatePaymentOption;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "application")
    private List<SubscriptionPlan> subscriptionPlans;

    public void updateKeys(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    public void authCheck(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new IllegalArgumentException("Access is denied");
        }
    }

    public void updateOptions(DuplicatePaymentOption duplicatePaymentOption) {
        this.duplicatePaymentOption = duplicatePaymentOption;
    }
}
