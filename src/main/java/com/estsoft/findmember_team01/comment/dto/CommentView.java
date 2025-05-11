package com.estsoft.findmember_team01.comment.dto;

import com.estsoft.findmember_team01.comment.domain.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentView {

    private Long id;
    private String writer;
    private String content;
    private LocalDateTime createdAt;

    public static CommentView from(Comment comment) {
        return CommentView.builder().id(comment.getCommentId())
            .writer(comment.getMember().getNickname()).content(comment.getContent())
            .createdAt(comment.getCreateAt()).build();
    }
}
