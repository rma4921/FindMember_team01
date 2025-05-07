package com.estsoft.findmember_team01.information.dto;

import com.estsoft.findmember_team01.information.domain.Information;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InformationView {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Long writerId;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static InformationView from(Information info) {
        return InformationView.builder()
            .id(info.getInformationId())
            .title(info.getTitle())
            .content(info.getContent())
            .writer(info.getMember().getNickname())
            .writerId(info.getMember().getId())
            .commentCount(info.getComments().size())
            .createdAt(info.getCreateAt())
            .modifiedAt(info.getUpdateAt())
            .build();
    }
}
