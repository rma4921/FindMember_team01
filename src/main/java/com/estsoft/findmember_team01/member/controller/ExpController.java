package com.estsoft.findmember_team01.member.controller;

import com.estsoft.findmember_team01.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpController {

    private final MemberService memberService;

    @PostMapping("/api/user/exp")
    public ResponseEntity<String> addExp(@RequestParam String email, @RequestParam int exp) {
        memberService.experienceHandler(email, exp);

        return ResponseEntity.ok("경험치가 추가되었습니다.");
    }
}
