package com.estsoft.findmember_team01.admin.controller;

import com.estsoft.findmember_team01.admin.dto.MemberViewResponse;
import com.estsoft.findmember_team01.admin.dto.UpdateLevelRequest;
import com.estsoft.findmember_team01.admin.service.AdminService;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.service.MemberService;
import com.estsoft.findmember_team01.recruitment.domain.Recruitment;
import com.estsoft.findmember_team01.recruitment.dto.RecruitmentRequest;
import com.estsoft.findmember_team01.recruitment.dto.RecruitmentResponse;
import com.estsoft.findmember_team01.recruitment.dto.RecruitmentViewResponse;
import com.estsoft.findmember_team01.recruitment.service.RecruitmentService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final RecruitmentService recruitmentService;

    @GetMapping("/api/admin/users")
    public String getMembers(@RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String keyword, Model model) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<Member> boardPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            boardPage = memberService.getMembersWithKeyword(keyword, pageable);
        } else {
            boardPage = memberService.getMembers(pageable);
        }

        List<MemberViewResponse> memberList = boardPage.getContent().stream()
            .map(MemberViewResponse::new).collect(Collectors.toList());

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

        return "admin/admin";
    }

    @ResponseBody
    @PutMapping("/api/admin/users/{id}/level")
    public ResponseEntity<String> updateUserLevel(@PathVariable Long id,
        @RequestParam UpdateLevelRequest request) {
        adminService.updateUserLevel(id, request.getLevel());
        return ResponseEntity.ok("레벨 및 권한이 수정되었습니다.");
    }

    @PostMapping("/api/admin/users/{id}/role")
    public String changeMemberRole(@PathVariable Long id, @RequestParam String role) {
        adminService.changeRole(id, role);

        return "redirect:/api/admin/users";
    }

    @GetMapping("/api/admin/posts")
    public String getRecruitments(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "-1") int status,
        @RequestParam(required = false) String keyword, Model model) {

        Page<Recruitment> boardPage;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasStatus = status != -1;

        if (hasKeyword && hasStatus) {
            boardPage = recruitmentService.getRecruitmentsByStatusAndKeyword(page, sortBy, status,
                keyword);
        } else if (hasKeyword) {
            boardPage = recruitmentService.getRecruitmentsWithKeyword(page, sortBy, keyword);
        } else if (hasStatus) {
            boardPage = recruitmentService.getRecruitmentsByStatus(page, sortBy, status);
        } else {
            boardPage = recruitmentService.getRecruitments(page, sortBy);
        }

        List<RecruitmentViewResponse> recruitmentList = boardPage.getContent().stream()
            .map(RecruitmentViewResponse::new).collect(Collectors.toList());

        int totalPages = boardPage.getTotalPages();
        int currentPage = page;
        int startPage = Math.max(0, (currentPage / 5) * 5);
        int endPage = Math.min(startPage + 4, totalPages - 1);

        model.addAttribute("recruitmentList", recruitmentList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("status", status);
        model.addAttribute("keyword", hasKeyword ? keyword : "");

        return "admin/adminPostsList";
    }

    @GetMapping("/api/admin/posts/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        RecruitmentResponse recruitment = recruitmentService.getRecruitmentById(id);
        model.addAttribute("recruitment", recruitment);

        return "admin/adminPostsDetail";
    }

    @PutMapping("/api/admin/posts/{id}")
    public String updateRecruitment(@PathVariable Long id,
        @ModelAttribute RecruitmentRequest requestDto) {
        recruitmentService.hideUpdate(id, requestDto);
        return "redirect:/api/admin/posts/" + id;
    }

    @DeleteMapping("/api/admin/posts/{id}")
    public String deleteRecruitmentByAdmin(@PathVariable Long id) {
        recruitmentService.deleteRecruitment(id);
        return "redirect:/api/admin/posts";
    }
}
