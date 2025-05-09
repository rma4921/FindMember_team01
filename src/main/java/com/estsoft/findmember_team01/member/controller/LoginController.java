package com.estsoft.findmember_team01.member.controller;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.dto.LoginRequest;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import com.estsoft.findmember_team01.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 회원 가입 메서드
    @PostMapping("/api/user/signup")
    public String signup(@ModelAttribute LoginRequest request) {
        memberService.save(request);
        return "redirect:/login";
    }

    // 회원 탈퇴 메서드
    @DeleteMapping("/api/user/{id}")
    public String deleteMember(@PathVariable Long id, Authentication authentication) {
        String currentEmail = authentication.getName();
        System.out.println("현재 이메일: " + currentEmail);
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(id + "는 존재하지 않는 사용자입니다."));

        if (!member.getEmail().equals(currentEmail)) {
            return "redirect:/api/posts";
        }

        memberService.deleteMember(id);
        SecurityContextHolder.clearContext();

        return "redirect:/login";
    }
}
