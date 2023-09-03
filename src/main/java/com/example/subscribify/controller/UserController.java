package com.example.subscribify.controller;

import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public Long signUp(@RequestBody @Validated CreateUserDto createUserDto) {
        userService.duplicateCheck(createUserDto);
        return userService.createUser(createUserDto);
    }


}
