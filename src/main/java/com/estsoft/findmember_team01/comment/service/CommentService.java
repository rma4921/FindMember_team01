package com.estsoft.findmember_team01.comment.service;

import com.estsoft.findmember_team01.comment.domain.Comment;
import com.estsoft.findmember_team01.comment.dto.CommentRequest;
import com.estsoft.findmember_team01.comment.dto.CommentResponse;
import com.estsoft.findmember_team01.comment.repository.CommentRepository;
import com.estsoft.findmember_team01.information.repository.InformationRepository;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final InformationRepository informationRepository;

    public CommentService(CommentRepository commentRepository,
        InformationRepository informationRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.informationRepository = informationRepository;
        this.memberRepository = memberRepository;
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public CommentResponse addComment(Long informationId, Long memberId,
        CommentRequest commentRequest) {
        var information = informationRepository.findById(informationId)
            .orElseThrow(() -> new RuntimeException("Information not found"));
        var member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        var comment = commentRepository.save(
            Comment.builder().content(commentRequest.getContent()).member(member)
                .information(information).build());

        return comment.toDto();
    }

    public List<CommentResponse> getAllComments(Long informationId) {
        return commentRepository.findByInformation_InformationId(informationId).stream()
            .map(Comment::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Comment updateComment(Long commentId, Long memberId, CommentRequest commentRequest) {
        var comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));
        if (!comment.getMember().getId().equals(memberId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
        comment.updateComment(commentRequest.getContent());
        return comment;
    }

    public void deleteCommentByAuthor(Long commentId, Long authorId) {
        var comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));
        var information = comment.getInformation();
        if (!information.getMember().getId().equals(authorId)) {
            throw new RuntimeException("글 작성자만 댓글을 삭제할 수 있습니다.");
        }
        commentRepository.delete(comment);
    }
}
