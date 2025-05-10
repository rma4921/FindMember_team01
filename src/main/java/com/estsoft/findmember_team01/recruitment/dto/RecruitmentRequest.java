package com.estsoft.findmember_team01.recruitment.dto;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.recruitment.domain.Recruitment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecruitmentRequest {

    private Long memberId;
    private String title;
    private String content;
    private LocalDateTime deadline;
    private Boolean status;
    private Boolean end_status;
    private Long level;
    private Boolean hide_status;

    @Builder
    public RecruitmentRequest(Long memberId, String title, String content, LocalDateTime deadline,
        Boolean status, Boolean end_status, Long level, Boolean hide_status) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.status = status;
        this.end_status = end_status;
        this.level = level;
        this.hide_status = hide_status;
    }

    public Recruitment toEntity(Member member) {
        return Recruitment.builder().member(member).title(this.title).content(this.content)
            .deadline(this.deadline).status(true).end_status(false).level(this.level)
            .hide_status(this.hide_status).build();
    }
}
