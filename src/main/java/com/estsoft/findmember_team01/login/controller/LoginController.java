package com.estsoft.findmember_team01.login.controller;

import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.login.dto.LoginRequest;
import com.estsoft.findmember_team01.member.domain.Member;
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

    @PostMapping("/api/user/signup")
    public String signup(@ModelAttribute LoginRequest request) {
        memberService.save(request);
        return "redirect:/login";
    }

    @DeleteMapping("/api/user/{id}")
    public String deleteMember(@PathVariable Long id, Authentication authentication) {
        String currentEmail = authentication.getName();
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.MEMBER_NOT_FOUND));

        if (!member.getEmail().equals(currentEmail)) {
            return "redirect:/api/posts";
        }

        memberService.deleteMember(id);
        SecurityContextHolder.clearContext();

        return "redirect:/login";
    }
}
