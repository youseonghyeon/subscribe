package com.example.subscribify.dto.api;

import com.example.subscribify.entity.Subscription;
import com.example.subscribify.entity.SubscriptionStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SubscriptionDetail {
    private Long id;
    private String subscribeName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer durationMonth;
    private SubscriptionStatus status;
    private Long price;

    /**
     * EXAMPLE
     *     {
     *         "id": 55,
     *         "subscribeName": "카카오톡 서랍장",
     *         "startDate": null,
     *         "endDate": null,
     *         "durationMonth": 6,
     *         "status": "PENDING",
     *         "price": 49000
     *     },
     * @param subscription
     */

    public SubscriptionDetail(Subscription subscription) {
        this.id = subscription.getId();
        this.subscribeName = subscription.getSubscribeName();
        this.startDate = subscription.getStartDate();
        this.endDate = subscription.getEndDate();
        this.durationMonth = subscription.getDurationMonth();
        this.status = subscription.getStatus();
        this.price = subscription.getPrice();
    }
}
