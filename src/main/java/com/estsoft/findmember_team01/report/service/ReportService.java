package com.estsoft.findmember_team01.report.service;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import com.estsoft.findmember_team01.report.domain.Report;
import com.estsoft.findmember_team01.report.dto.ReportRequest;
import com.estsoft.findmember_team01.report.dto.ReportResponse;
import com.estsoft.findmember_team01.report.repository.ReportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Report> getReport(int page, String sortBy) {
        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        return reportRepository.findAll(pageable);
    }

    public Page<Report> getReportByStatus(int page, String sortBy, int status) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());
        if (status == 0) {
            return reportRepository.findByStatus(false, pageable);  // 모집완료
        } else if (status == 1) {
            return reportRepository.findByStatus(true, pageable);  // 모집중
        }
        return reportRepository.findAll(pageable);  // 전체
    }

    public Page<Report> getReportWithKeyword(int page, String sortBy, String keyword) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());

        if (keyword != null && !keyword.isEmpty()) {
            return reportRepository.findByReasonContaining(keyword,
                pageable);
        } else {
            return reportRepository.findAll(pageable);
        }
    }

    public Page<Report> getReportByStatusAndKeyword(int page, String sortBy, int status,
        String keyword) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, sortBy));
        return reportRepository.findByStatusAndKeyword(status, keyword, pageable);
    }

    public ReportResponse getReportById(Long id) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 신고가 존재하지 않습니다: " + id));
        return ReportResponse.toEntity(report);
    }

    @Transactional
    public void updateStatus(Long reportId, boolean status) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("신고가 존재하지 않습니다. id=" + reportId));

        report.setStatus(status);
    }

}
