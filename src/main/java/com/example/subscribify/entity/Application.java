package com.example.subscribify.entity;

import com.example.subscribify.config.security.CustomUserDetails;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @OneToMany(mappedBy = "application", cascade = CascadeType.REMOVE)
    private List<SubscriptionPlan> subscriptionPlans;

    public Application updateApiKeys(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        return this;
    }

    public void authCheck() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long currentUserId = extractUserId(authentication);

        if (!isAdmin(authentication) && !isUserResourceOwner(currentUserId)) {
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

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> "ROLE_ADMIN".equals(grantedAuthority.getAuthority()));
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AccessDeniedException("Access is denied");
        }
        return ((CustomUserDetails) authentication.getPrincipal()).toUser().getId();
    }

    private boolean isUserResourceOwner(Long userId) {
        return this.user.getId().equals(userId);
    }

}
