package com.estsoft.findmember_team01.comment.dto;


import com.estsoft.findmember_team01.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {

    private Long memberId;
    private Long informationId;
    private Long commentId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public CommentResponse(Comment comment) {
        this.memberId = comment.getMember().getId();
        this.informationId = comment.getInformation().getInformationId();
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreateAt();
    }
}
