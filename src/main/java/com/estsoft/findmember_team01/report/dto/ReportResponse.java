package com.estsoft.findmember_team01.report.dto;

import com.estsoft.findmember_team01.report.domain.Report;
import com.estsoft.findmember_team01.report.domain.ReportTargetType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReportResponse {

    private Long reportId;
    private Long memberId;
    private ReportTargetType targetType;
    private Long targetId;
    private String reason;
    private LocalDateTime createdAt;
    private Boolean status;

    public static ReportResponse toEntity(Report report) {
        return ReportResponse.builder()
            .reportId(report.getReportId())
            .memberId(report.getMember().getId())
            .targetType(report.getTargetType())
            .targetId(report.getTargetId())
            .reason(report.getReason())
            .createdAt(report.getCreatedAt())
            .status(report.getStatus())
            .build();
    }

}
