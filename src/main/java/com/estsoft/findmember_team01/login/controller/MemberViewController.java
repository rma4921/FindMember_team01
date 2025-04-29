package com.estsoft.findmember_team01.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {
    
    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    // "/signup"
    @GetMapping("/signup")
    public String signUpView() {
        return "signup";
    }
}
