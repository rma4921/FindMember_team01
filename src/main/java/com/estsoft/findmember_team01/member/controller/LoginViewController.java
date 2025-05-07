package com.estsoft.findmember_team01.member.controller;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    private final MemberRepository memberRepository;

    public LoginViewController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

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
    public String mypageView(Model model, Authentication authentication) {
        String currentEmail = authentication.getName();
        Member member = memberRepository.findByEmail(currentEmail)
            .orElseThrow(() -> new IllegalStateException("회원 정보가 없습니다."));

        model.addAttribute("memberId", member.getId());
        return "mypage";
    }
}
