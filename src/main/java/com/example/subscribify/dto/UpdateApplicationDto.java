package com.example.subscribify.dto;

import com.example.subscribify.entity.DuplicatePaymentOption;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateApplicationDto {

    private DuplicatePaymentOption duplicatePaymentOption;

}
