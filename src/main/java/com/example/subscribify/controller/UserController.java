package com.example.subscribify.controller;

import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        // 테스트용 데모 데이터
        model.addAttribute("user", new CreateUserDto("testUser", "qwer1234", "qwer1234",
                "testMail@mail.com", "first", "last", "황새울로",
                "성남시", "서울", "12345", "대한민국"));

//        model.addAttribute("user", new CreateUserDto());
        return "user/signup";
    }


    @PostMapping("/signup")
    public String signUp(@ModelAttribute @Validated CreateUserDto createUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }

        // validation
        userService.duplicateCheck(createUserDto);
        // create user
        userService.createUser(createUserDto);

        return "redirect:/";
    }




}
