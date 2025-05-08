package com.estsoft.findmember_team01.application.domain;

import com.estsoft.findmember_team01.application.dto.RecruitmentRequest;
import com.estsoft.findmember_team01.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "recruitment")
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id")
    private Long recruitmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deadline;

    @Column(nullable = false)
    private Boolean status;

    private Long level;

    @Builder
    public Recruitment(Member member, String title, String content, LocalDateTime deadline,
        Boolean status, Long level) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.status = status;
        this.level = level;
    }

    public void updateFromDto(RecruitmentRequest dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.deadline = dto.getDeadline();
        this.status = dto.getStatus();
        this.level = dto.getLevel();
    }

    public Recruitment toEntity(Member member) {
        return Recruitment.builder()
            .member(member)
            .title(this.title)
            .content(this.content)
            .deadline(this.deadline)
            .status(this.status)
            .level(this.level)
            .build();
    }


}
