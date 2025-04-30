package com.estsoft.findmember_team01.information.controller;


import com.estsoft.findmember_team01.information.domain.Comment;
import com.estsoft.findmember_team01.information.dto.CommentRequest;
import com.estsoft.findmember_team01.information.dto.CommentResponse;
import com.estsoft.findmember_team01.information.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //댓글 생성
    @PostMapping("/api/information/{informationId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long informationId,
        @RequestParam Long memberId, @RequestBody CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.addComment(informationId, memberId,
            commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponse);
    }

    //댓글 조회
    @GetMapping("/api/information/{informationId}/comment")
    public ResponseEntity<List<CommentResponse>> getComment(@PathVariable Long informationId) {
        List<CommentResponse> commentResponse = commentService.getAllComments(informationId);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
    }

    //댓글 수정
    @PutMapping("/api/information/{informationId}/comment/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long informationId,
        @PathVariable Long commentId, @RequestParam Long memberId,
        @RequestBody CommentRequest commentRequest) {

        Comment updateComment = commentService.updateComment(informationId, commentId, memberId,
            commentRequest);
        return ResponseEntity.ok(updateComment.toDto());
    }

    //댓글 삭제
    @DeleteMapping("/api/information/{informationId}/comment/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long informationId,
        @PathVariable Long commentId, @RequestParam Long memberId) {
        commentService.deleteComment(informationId, commentId, memberId);
        return ResponseEntity.ok().build();
    }

}
