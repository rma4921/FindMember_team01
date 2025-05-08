package com.estsoft.findmember_team01.report.service;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import com.estsoft.findmember_team01.report.domain.Report;
import com.estsoft.findmember_team01.report.dto.ReportRequest;
import com.estsoft.findmember_team01.report.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void submitReport(Long memberId, ReportRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("신고자 정보를 찾을 수 없습니다: " + memberId));

        Report report = Report.builder()
            .member(member)
            .targetType(request.getTargetType())
            .targetId(request.getTargetId())
            .reason(request.getReason())
            .status(false)
            .build();

        reportRepository.save(report);
    }

}
