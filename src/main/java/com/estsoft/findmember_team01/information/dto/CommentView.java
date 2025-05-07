package com.estsoft.findmember_team01.information.dto;

import com.estsoft.findmember_team01.information.domain.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentView {
    private String writer;
    private String content;
    private LocalDateTime createdAt; // timeAgo 안 쓰고 날짜 그대로

    public static CommentView from(Comment comment) {
        return CommentView.builder()
            .writer(comment.getMember().getNickname())
            .content(comment.getContent())
            .createdAt(comment.getCreateAt())
            .build();
    }
}
