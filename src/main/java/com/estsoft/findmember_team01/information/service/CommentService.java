package com.estsoft.findmember_team01.information.service;


import com.estsoft.findmember_team01.information.domain.Comment;
import com.estsoft.findmember_team01.information.domain.Information;
import com.estsoft.findmember_team01.information.domain.Member;
import com.estsoft.findmember_team01.information.dto.CommentRequest;
import com.estsoft.findmember_team01.information.dto.CommentResponse;
import com.estsoft.findmember_team01.information.repository.CommentRepository;
import com.estsoft.findmember_team01.information.repository.InformationRepository;
import com.estsoft.findmember_team01.information.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final MemberRepository memberRepository;
    private CommentRepository commentRepository;
    private InformationRepository informationRepository;

    public CommentService(CommentRepository commentRepository,
        InformationRepository informationRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.informationRepository = informationRepository;
        this.memberRepository = memberRepository;
    }

    //댓글 생성
    public CommentResponse addComment(Long informationId, Long memberId,
        CommentRequest commentRequest) {
        Information information = informationRepository.findById(informationId)
            .orElseThrow(() -> new RuntimeException("Information not found"));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        Comment comment = commentRepository.save(
            Comment.builder().content(commentRequest.getContent()).member(member)
                .information(information).build());

        return comment.toDto();
    }

    //댓글 조회
    public List<CommentResponse> getAllComments(Long informationId) {
        List<Comment> comments = commentRepository.findByInformation_InformationId(informationId);
        return comments.stream().map(Comment::toDto).collect(Collectors.toList());
    }

    //댓글 수정
    @Transactional
    public Comment updateComment(Long informationId, Long commentId, Long memberId,
        CommentRequest commentRequest) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found" + commentId));

        // 작성자(memberId) 체크
        if (!comment.getMember().getMemberId().equals(memberId)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        comment.updateComment(commentRequest.getContent());

        return comment;
    }

    //댓글 삭제
    public void deleteComment(Long informationId, Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));

        // 작성자(memberId) 체크
        if (!comment.getMember().getMemberId().equals(memberId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

}
