package com.estsoft.findmember_team01.application.controller;

import com.estsoft.findmember_team01.application.dto.RecruitmentRequest;
import com.estsoft.findmember_team01.application.dto.RecruitmentResponse;
import com.estsoft.findmember_team01.application.service.RecruitmentService;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import com.estsoft.findmember_team01.member.service.MemberService;
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
    private final MemberService memberService;


    // 모집글 작성 처리
    @PostMapping("/create")
    public String createRecruitment(@ModelAttribute RecruitmentRequest requestDto,
        HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");

        if (memberId == null) {
            return "redirect:/login"; // 로그인 안 했을 경우
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        requestDto.setMemberId(member.getId());

        recruitmentService.saveRecruitment(requestDto);

        return "redirect:/api/posts";
    }


    // 모집글 상세 페이지
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model, HttpSession session) {
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

        return "detail";
    }


    //모집글 수정
    @PutMapping("/{id}")
    public String updateRecruitment(@PathVariable Long id,
        @ModelAttribute RecruitmentRequest requestDto) {
        recruitmentService.update(id, requestDto);
        return "redirect:/api/posts/" + id;
    }

    //모집글 삭제
    @DeleteMapping("/{id}")
    public String deleteRecruitment(@PathVariable Long id) {
        recruitmentService.deleteRecruitment(id);
        return "redirect:/api/posts";
    }


}
