package com.example.subscribify.controller;

import com.example.subscribify.domain.SessionUser;
import com.example.subscribify.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

//    private final UserService userService;


    @GetMapping("/mypage")
    public String myPage(@SessionUser User user, Model model) {
        model.addAttribute("user", user);
        return "mypage/mypage";
    }
}
