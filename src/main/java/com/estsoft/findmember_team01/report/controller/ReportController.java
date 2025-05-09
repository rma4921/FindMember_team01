package com.estsoft.findmember_team01.report.controller;

import com.estsoft.findmember_team01.report.domain.Report;
import com.estsoft.findmember_team01.information.service.CommentService;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.report.domain.ReportTargetType;
import com.estsoft.findmember_team01.report.dto.ReportRequest;
import com.estsoft.findmember_team01.report.dto.ReportResponse;
import com.estsoft.findmember_team01.report.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final CommentService commentService;

    @PostMapping("/api/posts/{id}/report")
    public String submitReport(@PathVariable("id") Long id, @ModelAttribute ReportRequest request,
        HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        reportService.submitReport(memberId, request);

        return "redirect:/api/posts/" + id;
    }

    //신고 목록 조회
    @GetMapping("/api/admin/posts")
    public String getReport(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "-1") int status,
        @RequestParam(required = false) String keyword,
        Model model,
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        model.addAttribute("memberId", session.getAttribute("memberId"));
        model.addAttribute("nickname", session.getAttribute("memberNickname"));

        Page<Report> boardPage;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasStatus = status != -1;

        if (hasKeyword && hasStatus) {
            // status, keyword 둘 다 필터링
            boardPage = reportService.getReportByStatusAndKeyword(page, sortBy, status,
                keyword);
        } else if (hasKeyword) {
            // keyword 필터링
            boardPage = reportService.getReportWithKeyword(page, sortBy, keyword);
        } else if (hasStatus) {
            // status 필터링
            boardPage = reportService.getReportByStatus(page, sortBy, status);
        } else {
            // 아무것도 없으면 전체 조회
            boardPage = reportService.getReport(page, sortBy);
        }

        List<ReportResponse> reportList = boardPage.getContent()
            .stream()
            .map(ReportResponse::toEntity)
            .collect(Collectors.toList());

        int totalPages = boardPage.getTotalPages();
        int currentPage = page;
        int startPage = Math.max(0, (currentPage / 5) * 5);
        int endPage = Math.min(startPage + 4, totalPages - 1);

        model.addAttribute("reportList", reportList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("status", status);
        model.addAttribute("keyword", hasKeyword ? keyword : "");

        return "reportList";
    }

    @GetMapping("/api/admin/posts/{id}")
    public String reportDetail(@PathVariable("id") Long id, Model model) {
        ReportResponse report = reportService.getReportById(id);  // 이름 변경
        model.addAttribute("report", report);  // 모델에 report로 저장

        return "reportDetail";
    }

    @PutMapping("/api/admin/posts/{id}")
    public String updateReportStatus(@PathVariable Long id, @RequestParam boolean status) {
        reportService.updateStatus(id, status);
        return "redirect:/api/admin/posts/" + id;
    }

    @DeleteMapping("/api/admin/posts/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/api/admin/posts";
    }

    @PostMapping("/information/report")
    public String handleReport(@ModelAttribute ReportRequest request,
        @AuthenticationPrincipal Member loginMember) {

        reportService.submitReport(loginMember.getId(), request);

        String redirectUrl;
        if (request.getTargetType() == ReportTargetType.POST) {
            redirectUrl = "/information/" + request.getTargetId();
        } else if (request.getTargetType() == ReportTargetType.COMMENT) {
            Long postId = commentService.findById(request.getTargetId())
                .getInformation()
                .getInformationId();
            redirectUrl = "/information/" + postId;
        } else {
            throw new IllegalArgumentException("지원하지 않는 신고 타입입니다.");
        }

        return "redirect:" + redirectUrl;
    }

}
