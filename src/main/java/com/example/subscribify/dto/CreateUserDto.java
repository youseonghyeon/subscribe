package com.example.subscribify.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{5,15}$", message = "유효하지 않은 아이디입니다.")
    private String username;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
}
