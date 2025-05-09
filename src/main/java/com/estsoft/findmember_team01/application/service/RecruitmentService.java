package com.estsoft.findmember_team01.application.service;

import com.estsoft.findmember_team01.application.domain.Application;
import com.estsoft.findmember_team01.application.domain.ApplicationStatus;
import com.estsoft.findmember_team01.application.domain.Recruitment;
import com.estsoft.findmember_team01.application.dto.RecruitmentRequest;
import com.estsoft.findmember_team01.application.dto.RecruitmentResponse;
import com.estsoft.findmember_team01.application.repository.ApplicationRepository;
import com.estsoft.findmember_team01.application.repository.RecruitmentRepository;
import com.estsoft.findmember_team01.exception.NotExistsIdException;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;
    private final UserDetailsService userDetailsService;

    public List<RecruitmentResponse> getAllRecruitments() {
        List<Recruitment> recruitments = recruitmentRepository.findAll();
        return recruitments.stream()
            .map(RecruitmentResponse::new)
            .toList();
    }


    @Transactional
    public void saveRecruitment(RecruitmentRequest requestDto) {
        if (requestDto.getMemberId() == null) {
            throw new IllegalArgumentException("작성자 정보가 없습니다.");
        }

        Member member = memberRepository.findById(requestDto.getMemberId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Recruitment recruitment = requestDto.toEntity(member);
        recruitmentRepository.save(recruitment);
    }


    @Transactional
    public RecruitmentResponse getRecruitmentById(Long id) {
        Recruitment recruitment = recruitmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 없습니다: " + id));
        System.out.println("레벨: " + recruitment.getLevel());
        System.out.println("세션:");
        return new RecruitmentResponse(recruitment);
    }

    @Transactional
    public void update(Long id, RecruitmentRequest requestDto) {
        Recruitment recruitment = recruitmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 없습니다: " + id));

        boolean beforeEndStatus = recruitment.getEnd_status();
        recruitment.updateFromDto(requestDto);

        if (!beforeEndStatus && Boolean.TRUE.equals(recruitment.getEnd_status())) {
            List<Application> acceptedList = applicationRepository.findByRecruitment_RecruitmentIdAndStatus(
                id, ApplicationStatus.ACCEPTED);

            for (Application app : acceptedList) {
                Member applicant = app.getMember();
                applicant.addExp(20);
                memberRepository.save(applicant);
            }

            Member writer = recruitment.getMember();
            writer.addExp(20);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null &&
                auth.getName().equals(writer.getEmail())
            ) {
                UserDetails user = userDetailsService.loadUserByUsername(writer.getEmail());
                Authentication newAuth = new UsernamePasswordAuthenticationToken(user,
                    auth.getCredentials(), user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
            memberRepository.save(writer);

        }
    }


    @Transactional
    public void deleteRecruitment(Long id) {
        recruitmentRepository.deleteById(id);
    }

    public Page<Recruitment> getRecruitments(int page, String sortBy) {
        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        return recruitmentRepository.findAll(pageable);
    }

    public Recruitment findPostById(Long id) {
        return recruitmentRepository.findById(id).orElseThrow(() -> new NotExistsIdException(id));
    }

    public Page<Recruitment> getRecruitmentsByStatus(int page, String sortBy, int status) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());
        if (status == 0) {
            return recruitmentRepository.findByStatus(false, pageable);  // 모집완료
        } else if (status == 1) {
            return recruitmentRepository.findByStatus(true, pageable);  // 모집중
        }
        return recruitmentRepository.findAll(pageable);  // 전체
    }

    public Page<Recruitment> getRecruitmentsWithKeyword(int page, String sortBy, String keyword) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());

        if (keyword != null && !keyword.isEmpty()) {
            return recruitmentRepository.findByTitleContainingOrContentContaining(keyword, keyword,
                pageable);
        } else {
            return recruitmentRepository.findAll(pageable);
        }
    }

    public Page<Recruitment> getRecruitmentsByStatusAndKeyword(int page, String sortBy, int status,
        String keyword) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, sortBy));
        return recruitmentRepository.findByStatusAndKeyword(status, keyword, pageable);
    }

    @Transactional
    public void hideUpdate(Long id, RecruitmentRequest requestDto) {
        Recruitment recruitment = recruitmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (requestDto.getHide_status() != null) {
            recruitment.setHide_status(requestDto.getHide_status());
        }
    }

}
