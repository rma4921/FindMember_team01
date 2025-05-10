package com.estsoft.findmember_team01.recruitment.controller;

import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import com.estsoft.findmember_team01.recruitment.dto.RecruitmentRequest;
import com.estsoft.findmember_team01.recruitment.dto.RecruitmentResponse;
import com.estsoft.findmember_team01.recruitment.service.RecruitmentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
