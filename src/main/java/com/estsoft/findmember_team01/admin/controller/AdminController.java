package com.estsoft.findmember_team01.admin.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.estsoft.findmember_team01.admin.dto.MemberViewResponse;
import com.estsoft.findmember_team01.admin.dto.UpdateLevelRequest;
import com.estsoft.findmember_team01.admin.service.AdminService;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.service.MemberService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;

    // 전체 회원 목록 조회
    @GetMapping("/api/admin/users")
    public String getMembers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String keyword,
        Model model
    ) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<Member> boardPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            boardPage = memberService.getMembersWithKeyword(keyword, pageable);
        } else {
            boardPage = memberService.getMembers(pageable);
        }

        List<MemberViewResponse> memberList = boardPage.getContent()
            .stream()
            .map(MemberViewResponse::new)
            .collect(Collectors.toList());

        int totalPages = boardPage.getTotalPages();
        int currentPage = page;
        int startPage = Math.max(0, (currentPage / 5) * 5);
        int endPage = Math.min(startPage + 4, totalPages - 1);

        model.addAttribute("memberList", memberList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("keyword", keyword != null ? keyword : "");

        return "admin";
    }

    // 사용자 레벨 조정 메서드
    @PutMapping("/api/admin/users/{id}/level")
    public ResponseEntity<String> updateUserLevel(@PathVariable Long id,
        @RequestParam UpdateLevelRequest request) {
        adminService.updateUserLevel(id, request.getLevel());
        return ResponseEntity.ok("레벨 및 권한이 수정되었습니다.");
    }
}
