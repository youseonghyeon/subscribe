package com.example.subscribify.dto;

import com.example.subscribify.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateApplicationDto {

    private String icon;
    private String name;
    private String businessName;
    private User user;
}
