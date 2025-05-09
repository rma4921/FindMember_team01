package com.estsoft.findmember_team01.information.service;

import com.estsoft.findmember_team01.information.domain.Comment;
import com.estsoft.findmember_team01.information.domain.Report;
import com.estsoft.findmember_team01.information.domain.TargetType;
import com.estsoft.findmember_team01.information.repository.ReportRepository;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final CommentService commentService;

    @Transactional
    public void submitReport(Long memberId, TargetType targetType, Long targetId, String reason) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("신고자 정보가 없습니다."));

        // 중복 신고 방지
        boolean alreadyReported = reportRepository.existsByMemberIdAndTargetTypeAndTargetId(
            memberId, targetType, targetId);

        if (alreadyReported) {
            throw new IllegalStateException("이미 신고한 대상입니다.");
        }

        Report report = Report.builder()
            .member(member)
            .targetType(targetType)
            .targetId(targetId)
            .reason(reason)
            .build();

        reportRepository.save(report);
    }

    public Long findPostIdByCommentId(Long commentId) {
        return commentService.findById(commentId)
            .getInformation()
            .getInformationId();
    }

}