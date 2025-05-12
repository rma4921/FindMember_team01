package com.estsoft.findmember_team01.report.controller;

import com.estsoft.findmember_team01.comment.dto.CommentView;
import com.estsoft.findmember_team01.comment.service.CommentService;
import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.information.domain.Information;
import com.estsoft.findmember_team01.information.domain.Status;
import com.estsoft.findmember_team01.information.dto.InformationView;
import com.estsoft.findmember_team01.information.service.InformationService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final InformationService informationService;

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

    @GetMapping("/api/admin/information")
    public String getAdminInformationList(@RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String status, Model model) {
        Status filterStatus = null;
        if ("SOLVED".equalsIgnoreCase(status)) {
            filterStatus = Status.SOLVED;
        } else if ("UNSOLVED".equalsIgnoreCase(status)) {
            filterStatus = Status.UNSOLVED;
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by("createAt").descending());
        Page<Information> pageInfo = informationService.searchByStatusAndKeywordPaged(filterStatus,
            keyword, pageable);

        List<InformationView> informationList = pageInfo.getContent().stream()
            .map(InformationView::from).toList();

        model.addAttribute("informationList", informationList);
        model.addAttribute("currentPage", pageInfo.getNumber());
        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedStatus", status);

        return "adminInformationList";
    }

    @GetMapping("/api/admin/information/{id}")
    public String getAdminInformationDetail(@PathVariable Long id, Model model) {
        Information info = informationService.findInformationById(id);

        model.addAttribute("post", InformationView.from(info));
        model.addAttribute("comments",
            info.getComments().stream().map(CommentView::from).collect(Collectors.toList()));

        return "adminInformationDetail";
    }

    @PostMapping("/api/admin/information/delete/{id}")
    public String deleteInformationByAdmin(@PathVariable Long id) {
        informationService.deleteInformation(id);
        return "redirect:/api/admin/information";
    }

    @PostMapping("/api/admin/comments/delete/{id}")
    public String deleteCommentByAdmin(@PathVariable Long id) {
        Long postId = commentService.findById(id).getInformation().getInformationId();
        commentService.deleteComment(id);
        return "redirect:/api/admin/information/" + postId;
    }

    @PutMapping("/api/admin/information/hide/{id}")
    public String toggleInformationHide(@PathVariable Long id, @RequestParam boolean hide_status) {
        informationService.updateHideStatus(id, hide_status);
        return "redirect:/api/admin/information/" + id;
    }

    @PutMapping("/api/admin/comments/hide/{id}")
    public String toggleCommentHide(@PathVariable Long id, @RequestParam boolean hide_status) {
        commentService.updateHideStatus(id, hide_status);
        Long infoId = commentService.findById(id).getInformation().getInformationId();
        return "redirect:/api/admin/information/" + infoId;
    }
}
