package com.estsoft.findmember_team01.report.controller;

import com.estsoft.findmember_team01.comment.service.CommentService;
import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.report.domain.Report;
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
            throw new GlobalException(GlobalExceptionType.LOGIN_REQUIRED);
        }

        reportService.submitReport(memberId, request);

        return "redirect:/api/posts/" + id;
    }

    @GetMapping("/api/admin/reports")
    public String getReport(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "-1") int status,
        @RequestParam(required = false) String keyword, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("memberId", session.getAttribute("memberId"));
        model.addAttribute("nickname", session.getAttribute("memberNickname"));

        Page<Report> boardPage;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasStatus = status != -1;

        if (hasKeyword && hasStatus) {
            boardPage = reportService.getReportByStatusAndKeyword(page, sortBy, status, keyword);
        } else if (hasKeyword) {
            boardPage = reportService.getReportWithKeyword(page, sortBy, keyword);
        } else if (hasStatus) {
            boardPage = reportService.getReportByStatus(page, sortBy, status);
        } else {
            boardPage = reportService.getReport(page, sortBy);
        }

        List<ReportResponse> reportList = boardPage.getContent().stream()
            .map(ReportResponse::toEntity).collect(Collectors.toList());

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

        return "report/reportList";
    }

    @GetMapping("/api/admin/reports/{id}")
    public String reportDetail(@PathVariable("id") Long id, Model model) {
        ReportResponse report = reportService.getReportById(id);
        model.addAttribute("report", report);

        return "report/reportDetail";
    }

    @PutMapping("/api/admin/reports/{id}")
    public String updateReportStatus(@PathVariable Long id, @RequestParam boolean status) {
        reportService.updateStatus(id, status);
        return "redirect:/api/admin/reports/" + id;
    }

    @DeleteMapping("/api/admin/reports/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/api/admin/reports";
    }

    @PostMapping("/information/report")
    public String handleReport(@ModelAttribute ReportRequest request,
        @AuthenticationPrincipal Member loginMember) {

        reportService.submitReport(loginMember.getId(), request);

        String redirectUrl;
        if (request.getTargetType() == ReportTargetType.INFORMATION) {
            redirectUrl = "/information/" + request.getTargetId();
        } else if (request.getTargetType() == ReportTargetType.COMMENT) {
            Long postId = commentService.findById(request.getTargetId()).getInformation()
                .getInformationId();
            redirectUrl = "/information/" + postId;
        } else {
            throw new GlobalException(GlobalExceptionType.UNSUPPORTED_REPORT_TYPE);
        }

        return "redirect:" + redirectUrl;
    }
}
