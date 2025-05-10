package com.estsoft.findmember_team01.main.controller;

import com.estsoft.findmember_team01.recruitment.domain.Recruitment;
import com.estsoft.findmember_team01.recruitment.dto.RecruitmentViewResponse;
import com.estsoft.findmember_team01.recruitment.service.RecruitmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainpageController {

    private final RecruitmentService recruitmentService;

    public MainpageController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @GetMapping("/api/posts")
    public String getRecruitments(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "-1") int status,
        @RequestParam(required = false) String keyword, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("memberId", session.getAttribute("memberId"));
        model.addAttribute("nickname", session.getAttribute("memberNickname"));

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

        List<RecruitmentViewResponse> recruitmentList = boardPage.getContent().stream().filter(
                recruitment -> recruitment.getHide_status() == null || !recruitment.getHide_status())
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

        return "mainpage";
    }
}

