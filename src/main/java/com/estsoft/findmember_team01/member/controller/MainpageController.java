package com.estsoft.findmember_team01.member.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.estsoft.findmember_team01.application.domain.Recruitment;
import com.estsoft.findmember_team01.application.dto.RecruitmentViewResponse;
import com.estsoft.findmember_team01.application.service.RecruitmentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainpageController {

	private final RecruitmentService recruitmentService;

	public MainpageController(RecruitmentService recruitmentService) {
		this.recruitmentService = recruitmentService;
	}

	// 전체 게시글 조회
	@GetMapping("/api/posts")
	public String getRecruitments(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "createdAt") String sortBy,
		@RequestParam(defaultValue = "-1") int status,
		@RequestParam(required = false) String keyword,
		Model model,
		HttpServletRequest request
	) {
		HttpSession session = request.getSession();
		model.addAttribute("memberId", session.getAttribute("memberId"));
		model.addAttribute("nickname", session.getAttribute("memberNickname"));

		Page<Recruitment> boardPage;

		boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
		boolean hasStatus = status != -1;

		if (hasKeyword && hasStatus) {
			// status, keyword 둘 다 필터링
			boardPage = recruitmentService.getRecruitmentsByStatusAndKeyword(page, sortBy, status,
				keyword);
		} else if (hasKeyword) {
			// keyword 필터링
			boardPage = recruitmentService.getRecruitmentsWithKeyword(page, sortBy, keyword);
		} else if (hasStatus) {
			// status 필터링
			boardPage = recruitmentService.getRecruitmentsByStatus(page, sortBy, status);
		} else {
			// 아무것도 없으면 전체 조회
			boardPage = recruitmentService.getRecruitments(page, sortBy);
		}

		List<RecruitmentViewResponse> recruitmentList = boardPage.getContent()
			.stream()
			.map(RecruitmentViewResponse::new)
			.collect(Collectors.toList());

		int totalPages = boardPage.getTotalPages();
		int currentPage = page;
		int startPage = Math.max(0, (currentPage / 5) * 5);
		int endPage = Math.min(startPage + 4, totalPages - 1);

		model.addAttribute("recruitmentList", recruitmentList);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("status", status);
		model.addAttribute("keyword", hasKeyword ? keyword : "");

		return "mainpage";
	}

}

