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
    private Double discount;
    private DiscountUnit discountType; // Percent, Fixed, None
    private Long discountedPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void update(String subscribeName, Integer duration, DurationUnit durationUnit, Long price, Double discount, DiscountUnit discountType, Long discountedPrice) {
        this.planName = subscribeName;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.price = price;
        this.discount = discount;
        this.discountType = discountType;
        assert (discountedPrice.equals(calculateDiscountPrice()));
        this.discountedPrice = discountedPrice;
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
