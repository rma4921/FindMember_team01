package com.estsoft.findmember_team01.member.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String loginView(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/main";
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signUpView() {
        return "signup";
    }

    @GetMapping("/main")
    public String mainView() {
        return "main";
    }

    // 마이페이지 테스트용
    @GetMapping("/mypage")
    public String mypageView(Model model, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");

        model.addAttribute("memberId", memberId);
        return "mypage";
    }
}
