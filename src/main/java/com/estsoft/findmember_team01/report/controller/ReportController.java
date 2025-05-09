package com.estsoft.findmember_team01.report.controller;

import com.estsoft.findmember_team01.information.service.CommentService;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.report.domain.ReportTargetType;
import com.estsoft.findmember_team01.report.dto.ReportRequest;
import com.estsoft.findmember_team01.report.service.ReportService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
