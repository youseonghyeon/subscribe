package com.example.subscribify.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{5,15}$", message = "유효하지 않은 아이디입니다.")
    private String username;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotEmpty
    private String passwordConfirm;
    @Email
    private String email;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String address;
    @NotEmpty
    private String city;
    @NotEmpty
    private String state;
    @NotEmpty
    private String zip;
    @NotEmpty
    private String country;

    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    public boolean isPasswordMatch() {
        return password.equals(passwordConfirm);
    }
}
