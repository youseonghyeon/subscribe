package com.example.subscribify.dto.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationDto {

    private String icon;
    @NotEmpty
    private String name;
    @NotEmpty
    private String businessName;
}
