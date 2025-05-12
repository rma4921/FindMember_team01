package com.estsoft.findmember_team01.application.controller;

import com.estsoft.findmember_team01.application.domain.Application;
import com.estsoft.findmember_team01.application.domain.ApplicationStatus;
import com.estsoft.findmember_team01.application.dto.ApplicationRequest;
import com.estsoft.findmember_team01.application.dto.ApplicationResponse;
import com.estsoft.findmember_team01.application.repository.ApplicationRepository;
import com.estsoft.findmember_team01.application.service.ApplicationService;
import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.recruitment.domain.Recruitment;
import com.estsoft.findmember_team01.recruitment.service.RecruitmentService;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
@RequestMapping("/api/posts")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final RecruitmentService recruitmentService;

    public ApplicationController(ApplicationService applicationService,
        ApplicationRepository applicationRepository, RecruitmentService recruitmentService) {
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.recruitmentService = recruitmentService;
    }

    @PostMapping("/{recruitmentId}/apply")
    public String applyToRecruitment(@PathVariable Long recruitmentId,
        @ModelAttribute ApplicationRequest requestDto, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");

        if (memberId == null) {
            return "redirect:/login";
        }

        requestDto.setRecruitmentId(recruitmentId);
        requestDto.setMemberId(memberId);

        applicationService.applyToRecruitment(requestDto);

        return "redirect:/api/posts/" + recruitmentId;
    }

    @GetMapping("/{recruitmentId}/apply")
    public String showApplications(@PathVariable Long recruitmentId,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(required = false) String titleKeyword,
        @RequestParam(defaultValue = "latest") String sort, Model model, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            throw new GlobalException(GlobalExceptionType.MEMBER_NOT_FOUND);
        }
        Recruitment recruitment = recruitmentService.findPostById(recruitmentId);
        if (!memberId.equals(recruitment.getMember().getId())) {
            throw new GlobalException(GlobalExceptionType.Cannot_Access);
        }
        Page<Application> applicationPage = applicationService.searchApplications(recruitmentId,
            titleKeyword, sort, page, 10);

        Map<String, String> paramMap = new HashMap<>();
        if (titleKeyword != null && !titleKeyword.isBlank()) {
            paramMap.put("titleKeyword", titleKeyword);
        }
        paramMap.put("sort", sort);

        model.addAttribute("page", applicationPage);
        model.addAttribute("applicationList", applicationPage.getContent());
        model.addAttribute("recruitmentId", recruitmentId);
        model.addAttribute("param", paramMap);

        return "application/ApplicationList";
    }

    @GetMapping("/{recruitmentId}/apply/{id}")
    public String detailApplication(@PathVariable Long recruitmentId,
        @PathVariable Long id,
        Model model,
        HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            throw new GlobalException(GlobalExceptionType.MEMBER_NOT_FOUND);
        }
        Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.APPLICATION_NOT_FOUND));
        // 해당 지원서의 모집글 ID가 URL의 recruitmentId와 일치하는지 검증 (보안 강화)
        if (!application.getRecruitment().getRecruitmentId().equals(recruitmentId)) {
            throw new GlobalException(GlobalExceptionType.Cannot_Access);
        }
        // 로그인한 사용자가 모집글 작성자인지 확인
        if (!application.getRecruitment().getMember().getId().equals(memberId)) {
            throw new GlobalException(GlobalExceptionType.Cannot_Access);
        }
        ApplicationResponse dto = applicationService.getDetailApplication(application);
        model.addAttribute("applicationDto", dto);
        return "application/ApplicationDetail";
    }


    @PutMapping("/{recruitmentId}/apply/{id}")
    public String updateApplicationStatus(@PathVariable Long id,
        @RequestParam("status") ApplicationStatus status) {
        applicationService.updateStatus(id, status);
        return "redirect:/api/posts/{recruitmentId}/apply";
    }

    @DeleteMapping("/{recruitmentId}/apply/{id}")
    public String deleteApplication(@PathVariable Long id) {
        applicationRepository.deleteById(id);
        return "redirect:/api/posts/{recruitmentId}/apply";
    }
}
