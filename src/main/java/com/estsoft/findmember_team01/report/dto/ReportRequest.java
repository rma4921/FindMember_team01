package com.estsoft.findmember_team01.report.dto;

import com.estsoft.findmember_team01.report.domain.ReportTargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportRequest {

    private Long memberId; // 신고한 사람 ID
    private ReportTargetType targetType;
    private Long targetId;
    private String reason;
}
