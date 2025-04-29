package com.estsoft.findmember_team01.login.controller;

import com.estsoft.findmember_team01.login.dto.AddMemberRequest;
import com.estsoft.findmember_team01.login.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/user/signup")
    public String signup(@ModelAttribute AddMemberRequest request) {
        memberService.save(request);
        return "redirect:/login";
    }
}
