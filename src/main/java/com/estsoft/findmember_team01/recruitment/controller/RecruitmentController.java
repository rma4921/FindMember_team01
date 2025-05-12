package com.estsoft.findmember_team01.recruitment.controller;

import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import com.estsoft.findmember_team01.recruitment.domain.Recruitment;
import com.estsoft.findmember_team01.recruitment.dto.RecruitmentRequest;
import com.estsoft.findmember_team01.recruitment.dto.RecruitmentResponse;
import com.estsoft.findmember_team01.recruitment.dto.RecruitmentViewResponse;
import com.estsoft.findmember_team01.recruitment.service.RecruitmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;
    private final MemberRepository memberRepository;

    @PostMapping("/create")
    public String createRecruitment(@ModelAttribute RecruitmentRequest requestDto,
        HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");

        if (memberId == null) {
            return "redirect:/login";
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.MEMBER_NOT_FOUND));

        requestDto.setMemberId(member.getId());

        recruitmentService.saveRecruitment(requestDto);

        return "redirect:/api/posts";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        RecruitmentResponse recruitment = recruitmentService.getRecruitmentById(id);
        model.addAttribute("recruitment", recruitment);
        model.addAttribute("memberId", memberId);

        if (memberId != null) {
            Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GlobalException(GlobalExceptionType.MEMBER_NOT_FOUND));
            Long memberLevel = member.getLevel();
            model.addAttribute("memberLevel", memberLevel);
        }

        return "detail";
    }

    @PutMapping("/{id}")
    public String updateRecruitment(@PathVariable Long id,
        @ModelAttribute RecruitmentRequest requestDto) {
        recruitmentService.update(id, requestDto);
        return "redirect:/api/posts/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteRecruitment(@PathVariable Long id) {
        recruitmentService.deleteRecruitment(id);
        return "redirect:/api/posts";
    }

    @GetMapping("/complete")
    public String getEndRecruitments(
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

        Page<Recruitment> boardPage;

        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasStatus = status != -1;

        if (hasKeyword && hasStatus) {
            // status, keyword 둘 다 필터링
            boardPage = recruitmentService.getRecruitmentsByStatusAndKeyword(page, sortBy, status,
                keyword);
        } else if (hasKeyword) {
            // keyword 필터링
            boardPage = recruitmentService.getRecruitmentsWithKeyword(page, sortBy, keyword);
        } else if (hasStatus) {
            // status 필터링
            boardPage = recruitmentService.getRecruitmentsByStatus(page, sortBy, status);
        } else {
            // 아무것도 없으면 전체 조회
            boardPage = recruitmentService.getRecruitments(page, sortBy);
        }

        List<RecruitmentViewResponse> recruitmentList = boardPage.getContent().stream()
            // 숨겨진 게시글 필터링
            .map(RecruitmentViewResponse::new)
            .collect(Collectors.toList());

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

        return "completePosts";
    }

    @GetMapping("/complete/{id}")
    public String completePostDetail(@PathVariable("id") Long id, Model model,
        HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        RecruitmentResponse recruitment = recruitmentService.getRecruitmentById(id);
        model.addAttribute("recruitment", recruitment);
        model.addAttribute("memberId", memberId);

        if (memberId != null) {
            Member member = memberRepository.findById(memberId)
                .orElseThrow(
                    () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다. id=" + memberId));
            Long memberLevel = member.getLevel();
            model.addAttribute("memberLevel", memberLevel);
        }

        return "completePostsDetail";
    }

}
