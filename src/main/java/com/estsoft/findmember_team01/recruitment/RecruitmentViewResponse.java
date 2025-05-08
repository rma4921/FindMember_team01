package com.estsoft.findmember_team01.recruitment;

import java.time.LocalDateTime;

import com.estsoft.findmember_team01.recruitment.Recruitment;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentViewResponse {

	private Long recruitmentId;
	private String title;
	private String content;
	private String nickname;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createAt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime deadline;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateAt;

	private Boolean status;
	private Long level;

	public RecruitmentViewResponse(Recruitment recruitment) {
		this.recruitmentId = recruitment.getRecruitmentId();
		this.title = recruitment.getTitle();
		this.content = recruitment.getContent();
		this.nickname = recruitment.getMember().getNickname();
		this.createAt = recruitment.getCreateAt();
		this.updateAt = recruitment.getUpdateAt();
		this.deadline = recruitment.getDeadline();
		this.status = recruitment.getStatus();
		this.level = recruitment.getLevel();
	}
}
