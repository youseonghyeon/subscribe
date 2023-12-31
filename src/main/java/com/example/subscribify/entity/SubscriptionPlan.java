package com.example.subscribify.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubscriptionPlan extends BaseTimeEntity{

    @Id
    @GeneratedValue
    private Long id;
    private String planName;
    private Integer duration;
    private DurationUnit durationUnit; // Month, Year
    private Long price;
    private DiscountUnit discountType; // Percent, Fixed, None
    private Double discount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Application application;

    public void update(String subscribeName, Integer duration, DurationUnit durationUnit, Long price, Double discount, DiscountUnit discountType) {
        this.planName = subscribeName;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.price = price;
        this.discount = discount;
        this.discountType = discountType;
    }

    private Long calculateDiscountPrice() {
        if (DiscountUnit.PERCENT.equals(this.discountType)) {
            return (long) (price * (1 - discount));
        } else if (DiscountUnit.FIXED.equals(this.discountType)) {
            return price - discount.longValue();
        } else {
            return price;
        }
    }

}
