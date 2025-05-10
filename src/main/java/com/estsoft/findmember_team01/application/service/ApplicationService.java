package com.estsoft.findmember_team01.application.service;

import com.estsoft.findmember_team01.application.domain.Application;
import com.estsoft.findmember_team01.application.domain.ApplicationStatus;
import com.estsoft.findmember_team01.application.domain.Recruitment;
import com.estsoft.findmember_team01.application.dto.ApplicationRequest;
import com.estsoft.findmember_team01.application.dto.ApplicationResponse;
import com.estsoft.findmember_team01.application.repository.ApplicationRepository;
import com.estsoft.findmember_team01.application.repository.RecruitmentRepository;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void applyToRecruitment(ApplicationRequest dto) {
        Recruitment recruitment = recruitmentRepository.findById(dto.getRecruitmentId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 모집글입니다."));

        Member member = memberRepository.findById(dto.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Application application = Application.builder().recruitment(recruitment).member(member)
            .content(dto.getContent()).status(ApplicationStatus.PENDING).build();

        applicationRepository.save(application);
    }

    @Transactional
    public List<Application> getApplicationsByRecruitmentId(Long recruitmentId) {
        return applicationRepository.findByRecruitment_RecruitmentId(recruitmentId);
    }

    @Transactional
    public ApplicationResponse getDetailApplication(Application application) {
        Member member = application.getMember();
        Recruitment recruitment = application.getRecruitment();

        return ApplicationResponse.builder().applicationId(application.getApplicationId())
            .title(recruitment.getTitle()).content(application.getContent())
            .createdAt(application.getCreatedAt()).status(application.getStatus())
            .applicantNickname(member.getNickname()).applicantEmail(member.getEmail())
            .recruitmentId(recruitment.getRecruitmentId()).build();
    }

    @Transactional
    public void updateStatus(Long id, ApplicationStatus status) {
        Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("지원서를 찾을 수 없습니다."));
        application.updateStatus(status);
    }

    public Page<Application> searchApplications(Long recruitmentId, String titleKeyword,
        String sort, int page, int size) {
        Sort sortOption = Sort.by("createdAt");
        if ("oldest".equals(sort)) {
            sortOption = sortOption.ascending();
        } else {
            sortOption = sortOption.descending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOption);
        String keyword = (titleKeyword == null || titleKeyword.isBlank()) ? null : titleKeyword;

        return applicationRepository.findBySearchConditions(recruitmentId, keyword, pageable);
    }
}
