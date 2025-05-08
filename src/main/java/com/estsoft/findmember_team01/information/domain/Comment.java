package com.estsoft.findmember_team01.information.domain;


import com.estsoft.findmember_team01.information.dto.CommentResponse;
import com.estsoft.findmember_team01.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "information_id", nullable = false)
    private Information information;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String content;

    @CreatedDate
    private LocalDateTime createAt;

    @Builder
    public Comment(String content, Information information, Member member) {
        this.content = content;
        this.information = information;
        this.member = member;
    }

    public void updateComment(String content) {
        this.content = content;
    }

    public CommentResponse toDto() {
        return new CommentResponse(this);
    }

}
