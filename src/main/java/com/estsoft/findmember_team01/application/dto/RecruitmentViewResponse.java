package com.estsoft.findmember_team01.application.dto;

import com.estsoft.findmember_team01.application.domain.Recruitment;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentViewResponse {

    private Long recruitmentId;
    private String title;
    private String content;
    private String nickname;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deadline;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private Boolean status;
    private Long level;

    public RecruitmentViewResponse(Recruitment recruitment) {
        this.recruitmentId = recruitment.getRecruitmentId();
        this.title = recruitment.getTitle();
        this.content = recruitment.getContent();
        this.nickname = recruitment.getMember().getNickname();
        this.createdAt = recruitment.getCreatedAt();
        this.updatedAt = recruitment.getUpdatedAt();
        this.deadline = recruitment.getDeadline();
        this.status = recruitment.getStatus();
        this.level = recruitment.getLevel();
    }
}
