package com.estsoft.findmember_team01.admin.dto;

import com.estsoft.findmember_team01.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberViewResponse {

    private Long id;
    private Long level;
    private String nickname;
    private Integer exp;
    private Long accessCount;
    private String role;
    private String userAgent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastAccessTime;

    public MemberViewResponse(Member member) {
        this.id = member.getId();
        this.level = member.getLevel();
        this.nickname = member.getNickname();
        this.exp = member.getExp();
        this.accessCount = member.getAccessCount();
        this.role = member.getRole();
        this.userAgent = member.getUserAgent();
        this.createdAt = member.getCreatedAt();
        this.lastAccessTime = member.getLastAccessTime();
    }
}
