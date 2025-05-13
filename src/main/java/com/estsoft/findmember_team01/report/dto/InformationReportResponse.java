package com.estsoft.findmember_team01.report.dto;

import com.estsoft.findmember_team01.report.domain.Report;
import com.estsoft.findmember_team01.report.domain.ReportTargetType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InformationReportResponse {

    private Long reportId;
    private Long memberId;
    private ReportTargetType targetType;
    private Long targetId;
    private String reason;
    private LocalDateTime createdAt;
    private Boolean status;
    private String nickname;
    private Long postId;

    public static InformationReportResponse from(Report report, Long postId) {
        return new InformationReportResponse(report.getReportId(), report.getMember().getId(),
            report.getTargetType(), report.getTargetId(), report.getReason(), report.getCreatedAt(),
            report.getStatus(), report.getMember().getNickname(), postId);

    }
}
