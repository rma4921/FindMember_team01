package com.estsoft.findmember_team01.application.dto;

import com.estsoft.findmember_team01.application.domain.ApplicationStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationResponse {

    private Long applicationId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private ApplicationStatus status;
    private String applicantNickname;
    private String applicantEmail;
    private Long recruitmentId;
}

