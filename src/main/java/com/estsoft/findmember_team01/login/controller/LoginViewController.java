package com.estsoft.findmember_team01.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginViewController {

    private final HttpServletRequest request;

    @GetMapping("/login")
    public String loginView(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/api/posts";
        }

        String referer = request.getHeader("Referer");

        if (referer != null && !referer.contains("/login") && !referer.contains("/signup")
            && !referer.contains("/logout")) {
            request.getSession().setAttribute("prevPage", referer);
        }

        return "login/login";
    }

    @GetMapping("/signup")
    public String signUpView() {
        return "login/signup";
    }

}
