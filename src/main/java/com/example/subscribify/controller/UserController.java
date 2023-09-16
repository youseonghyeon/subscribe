package com.example.subscribify.controller;

import com.example.subscribify.dto.controller.EnrollUserRequest;
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
        // 테스트를 위한 더미 데이터
        model.addAttribute("createUserDto", mockModel());
        // TODO: 테스트를 위한 더미 데이터를 사용하지 않을 경우 아래 코드로 변경
        // model.addAttribute("user", new CreateUserDto());
        return "user/signup";
    }

    // 테스트를 위한 임시 코드입니다. PROD 배포 시 꼭 삭제해주세요.
    private EnrollUserRequest mockModel() {
        int i = new Random().nextInt(1000) + 1;
        int j = new Random().nextInt(1000) + 1;
        return new EnrollUserRequest("testUser" + i, "qwer1234", "qwer1234",
                "testMail" + j + "@mail.com", "first", "last", "황새울로",
                "성남시", "서울", "12345", "대한민국");
    }


    @PostMapping("/signup")
    public String signUp(@ModelAttribute @Valid EnrollUserRequest enrollUserRequest, BindingResult bindingResult) {
        validatePasswordConfirmation(enrollUserRequest, bindingResult);
        // TODO 아래 validateUserUniqueness 는 "중복검사" 버튼을 추가해서 지우는 것으로 함
        // TODO 검증 로직은 service layer 에서 처리하는 것으로 변경해야 함
        validateUserUniqueness(enrollUserRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }

        userService.createUser(enrollUserRequest);
        return "redirect:/";
    }


    private void validatePasswordConfirmation(EnrollUserRequest enrollUserRequest, BindingResult bindingResult) {
        if (isNotPasswordMatchingConfirmation(enrollUserRequest)) {
            bindingResult.rejectValue("passwordConfirm", "invalid.passwordConfirm", "비밀번호가 일치하지 않습니다.");
        }
    }

    private boolean isNotPasswordMatchingConfirmation(EnrollUserRequest enrollUserRequest) {
        return !enrollUserRequest.getPassword().equals(enrollUserRequest.getPasswordConfirm());
    }


    private void validateUserUniqueness(EnrollUserRequest enrollUserRequest, BindingResult bindingResult) {
        // 쿼리가 2회 실행됨
        if (userService.isUsernameTaken(enrollUserRequest.getUsername())) {
            bindingResult.rejectValue("username", "invalid.username", "이미 사용중인 아이디입니다.");
        }
        if (userService.isEmailTaken(enrollUserRequest.getEmail())) {
            bindingResult.rejectValue("email", "invalid.email", "이미 사용중인 이메일입니다.");
        }
    }


}
