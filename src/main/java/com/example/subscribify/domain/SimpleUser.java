package com.example.subscribify.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUser {
    private Long id;
    private String username;
    private String password;
}
