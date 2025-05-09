package com.estsoft.findmember_team01.application.dto;

import com.estsoft.findmember_team01.application.domain.Recruitment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecruitmentResponse {

    private Long recruitmentId;
    private Long memberId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deadline;
    private Boolean status;
    private Boolean end_status;
    private Long level;
    private Boolean hide_status;

    @Builder
    public RecruitmentResponse(Recruitment recruitment) {
        this.recruitmentId = recruitment.getRecruitmentId();
        this.memberId = recruitment.getMember().getId();
        this.title = recruitment.getTitle();
        this.content = recruitment.getContent();
        this.createdAt = recruitment.getCreatedAt();
        this.updatedAt = recruitment.getUpdatedAt();
        this.deadline = recruitment.getDeadline();
        this.status = recruitment.getStatus();
        this.end_status = recruitment.getEnd_status();
        this.level = recruitment.getLevel();
        this.hide_status = recruitment.getHide_status();
    }
}
