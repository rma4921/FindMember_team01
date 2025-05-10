package com.estsoft.findmember_team01.comment.service;

import com.estsoft.findmember_team01.comment.domain.Comment;
import com.estsoft.findmember_team01.comment.dto.CommentRequest;
import com.estsoft.findmember_team01.comment.dto.CommentResponse;
import com.estsoft.findmember_team01.comment.repository.CommentRepository;
import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
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
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.COMMENT_NOT_FOUND));
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public CommentResponse addComment(Long informationId, Long memberId,
        CommentRequest commentRequest) {
        var information = informationRepository.findById(informationId)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.INFORMATION_NOT_FOUND));
        var member = memberRepository.findById(memberId)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.MEMBER_NOT_FOUND));

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
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.COMMENT_NOT_FOUND));
        if (!comment.getMember().getId().equals(memberId)) {
            throw new GlobalException(GlobalExceptionType.UNAUTHORIZED_ACTION);
        }
        comment.updateComment(commentRequest.getContent());
        return comment;
    }

    public void deleteCommentByAuthor(Long commentId, Long authorId) {
        var comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.COMMENT_NOT_FOUND));
        var information = comment.getInformation();
        if (!information.getMember().getId().equals(authorId)) {
            throw new GlobalException(GlobalExceptionType.ONLY_AUTHOR_CAN_DELETE);
        }
        commentRepository.delete(comment);
    }
}
