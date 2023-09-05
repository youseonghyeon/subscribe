package com.example.subscribify.dto;

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
    private String durationUnit; // Month, Year
    private Long price;
    private Double discount;
    private String discountType; // Percent, Fixed
    private Long discountedPrice;

}
