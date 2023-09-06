package com.example.subscribify.dto;

import com.example.subscribify.entity.DiscountUnit;
import com.example.subscribify.entity.DurationUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubscribeDto {

    private String subscribeName;
    private Integer duration;
    private DurationUnit durationUnit; // Month, Year
    private Long price;
    private Double discount;
    private DiscountUnit discountType; // Percent, Fixed
    private Long discountedPrice;

}
