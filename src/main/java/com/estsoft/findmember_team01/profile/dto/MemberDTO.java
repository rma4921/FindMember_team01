package com.estsoft.findmember_team01.profile.dto;

import com.estsoft.findmember_team01.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {

    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String tech;
    private String profileImageUrl;
    private long level;

    public static MemberDTO from(Member user) {
        return MemberDTO.builder().nickname(user.getNickname()).email(user.getEmail())
            .tech(user.getTech()).profileImageUrl(user.getProfileImageUrl()).level(user.getLevel())
            .build();
    }
}