package com.example.subscribify.dto;

import com.example.subscribify.entity.DiscountUnit;
import com.example.subscribify.entity.DurationUnit;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateSubscribeDto {

    private Long applicationId;
    private String subscribeName;
    private Integer duration;
    private DurationUnit durationUnit; // Month, Year
    private Long price;
    private Double discount;
    private DiscountUnit discountType; // Percent, Fixed
    private Long discountedPrice;

}
