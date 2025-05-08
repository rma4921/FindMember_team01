package com.estsoft.findmember_team01.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationRequest {

    private Long recruitmentId;
    private Long memberId;
    private String content;


    @Builder
    public ApplicationRequest(Long recruitmentId, Long memberId, String content) {
        this.recruitmentId = recruitmentId;
        this.memberId = memberId;
        this.content = content;
    }

}
