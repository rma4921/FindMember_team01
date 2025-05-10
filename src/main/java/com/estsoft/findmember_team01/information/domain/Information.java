package com.estsoft.findmember_team01.information.domain;

import com.estsoft.findmember_team01.information.dto.InformationResponse;
import com.estsoft.findmember_team01.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long informationId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.UNSOLVED;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "information", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Information(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void updateInformation(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public InformationResponse toDto() {
        return new InformationResponse(this);
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
