package com.example.subscribify.controller;

import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

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
        model.addAttribute("createUserDto", mockModel(model));

//        model.addAttribute("user", new CreateUserDto());
        return "user/signup";
    }

    private CreateUserDto mockModel(Model model) {
        int i = new Random().nextInt(1000) + 1;
        int j = new Random().nextInt(1000) + 1;
        return new CreateUserDto("testUser" + i, "qwer1234", "qwer1234",
                "testMail" + j + "@mail.com", "first", "last", "황새울로",
                "성남시", "서울", "12345", "대한민국");
    }


    @PostMapping("/signup")
    public String signUp(@ModelAttribute @Valid CreateUserDto createUserDto, BindingResult bindingResult) {
        validatePasswordConfirmation(createUserDto, bindingResult);
        validateUserUniqueness(createUserDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user/signup";
        }

        userService.createUser(createUserDto);
        return "redirect:/";
    }

    private void validatePasswordConfirmation(CreateUserDto createUserDto, BindingResult bindingResult) {
        if (isNotPasswordMatchingConfirmation(createUserDto)) {
            bindingResult.rejectValue("passwordConfirm", "invalid.passwordConfirm", "비밀번호가 일치하지 않습니다.");
        }
    }

    private void validateUserUniqueness(CreateUserDto createUserDto, BindingResult bindingResult) {
        if (!userService.isUserUnique(createUserDto)) {
            bindingResult.rejectValue("username", "invalid.username", "이미 사용중인 아이디입니다.");
        }
    }

    private boolean isNotPasswordMatchingConfirmation(CreateUserDto createUserDto) {
        return !createUserDto.getPassword().equals(createUserDto.getPasswordConfirm());
    }


}
