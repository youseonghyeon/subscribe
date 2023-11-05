package com.example.subscribify.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.access.AccessDeniedException;

import java.security.MessageDigest;
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

    public Application updateKeys(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        return this;
    }

    public void authCheck(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new AccessDeniedException("Access is denied");
        }
    }

    public void apiKeyCheck(String apiKey) {
        if (!MessageDigest.isEqual(this.apiKey.getBytes(), apiKey.getBytes())) {
            throw new AccessDeniedException("Access is denied");
        }
    }

    public void updateOptions(DuplicatePaymentOption duplicatePaymentOption) {
        this.duplicatePaymentOption = duplicatePaymentOption;
    }
}
