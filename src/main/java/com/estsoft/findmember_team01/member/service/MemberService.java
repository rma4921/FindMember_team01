package com.estsoft.findmember_team01.member.service;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.dto.LoginRequest;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.dto.LoginRequest;
import com.estsoft.findmember_team01.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder encoder;
	private final UserDetailsService userDetailsService;

	public Page<Member> getMembers(Pageable pageable) {
		return memberRepository.findAll(pageable);
	}

	// 멤버 닉네임 검색
	public Page<Member> getMembersWithKeyword(String keyword, Pageable pageable) {
		return memberRepository.findByKeyword(keyword, pageable);
	}

	// 회원가입 정보 저장
	public void save(LoginRequest dto) {
		System.out.println("SAVE 실행");
		String nickname = generateUniqueNickname();
		Member member = new Member(dto.getEmail(), encoder.encode(dto.getPassword()), nickname);
		member.updateRoleByLevel();
		memberRepository.save(member);
	}

	// 닉네임 자동 설정
	private String generateUniqueNickname() {
		String nickname;
		do {
			nickname = "user_" + UUID.randomUUID().toString().substring(0, 8);
		} while (memberRepository.existsByNickname(nickname));
		return nickname;
	}

	// 회원 탈퇴
	public void deleteMember(Long memberId) {
		memberRepository.deleteById(memberId);
	}

//    // 팀원 모집 table을 사용해야 함. 논의해보기
//    public void completeProject(Long projectId) {
//        int completeExp = 20;
//
//        Project project = projectRepository.findById(projectId)
//            .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다."));
//        // applicationRepository에 List<Application> findByRecruitmentIdAndStatusTrue(Long projectId); 추가해야 함.
//        List<Application> applications = applicationRepository.findByRecruitmentIdAndStatusTrue(projectId);
//
//        for (Application application : applications) {
//            Member member = application.getMember(); // application에 getMember() 구현해야 함. ManyToOne 설정되어야 함.
//            member.addExp(completeExp);
//            member.updateRoleByLevel();
//            memberRepository.save(member);
//
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            if (auth != null &&
//                auth.getName().equals(member.getEmail())
//            ) {
//                UserDetails user = userDetailsService.loadUserByUsername(member.getEmail());
//                Authentication newAuth = new UsernamePasswordAuthenticationToken(user,
//                    auth.getCredentials(), user.getAuthorities());
//
//                SecurityContextHolder.getContext().setAuthentication(newAuth);
//            }
//        }
//        // 여기도 맞춰야 함. 프로젝트
//        project.complete(true);
//        projectRepository.save(project);
//    }

	// 레벨에 따른 권한 부여 테스트
	public void experienceHandler(String email, int exp) {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException(email + "에 해당하는 정보를 찾을 수 없습니다."));

		member.addExp(exp);
		member.updateRoleByLevel();
		memberRepository.save(member);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		auth.getAuthorities().forEach(authority -> {
			System.out.println("처음 권한: " + authority.getAuthority());
		});

		if (auth != null && auth.getName().equals(email)) {
			UserDetails user = userDetailsService.loadUserByUsername(email);
			Authentication newAuth = new UsernamePasswordAuthenticationToken(user,
				auth.getCredentials(), user.getAuthorities());
			newAuth.getAuthorities().forEach(authority -> {
				System.out.println("수정된 권한: " + authority.getAuthority());
			});

			SecurityContextHolder.getContext().setAuthentication(newAuth);
		}
	}
}
