package com.estsoft.findmember_team01.member.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String email;

	@Column(nullable = false, length = 32)
	private String password;

	@Column(nullable = false, unique = true, length = 10)
	private String nickname;

	@Column(nullable = false)
	private Long level;

	@Column(length = 50)
	private String tech;

	@Column(nullable = false)
	private Integer exp;

	@CreatedDate
	private LocalDateTime createdAt;

	private LocalDateTime lastAccessTime;

	private Long accessCount;

	@Column(length = 200)
	private String userAgent;

	@Column(length = 10)
	private String role;

	// 생성자
	public Member(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.level = 1L;
		this.exp = 0;
		this.accessCount = 0L;
		this.role = "USER";
	}

	// 레벨에 따른 권한 부여
	public void updateRoleByLevel() {
		if (this.level == 0) {
			this.role = "ADMIN";
		} else if (this.level >= 7) {
			this.role = "MASTER";
		} else {
			this.role = "USER";
		}
	}

	// 로그인 시 정보 업데이트
	public void updateLoginInfo(String userAgent) {
		this.userAgent = userAgent;
		this.accessCount += 1;
		this.lastAccessTime = LocalDateTime.now();
	}

	// 로그아웃 시 정보 업데이트
	public void updateLogoutInfo() {
		this.lastAccessTime = LocalDateTime.now();
	}

	// 경험치 추가 로직
	public void addExp(int exp) {
		this.exp += exp;
		Long temp = this.level;
		this.level += exp / 10;
		this.exp %= 10;


        if (!level.equals(temp)) {
            updateRoleByLevel();
        }

    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(() -> "ROLE_" + this.role);
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	public void updateRole(String newRole) {
		this.role = newRole;
	}

}
