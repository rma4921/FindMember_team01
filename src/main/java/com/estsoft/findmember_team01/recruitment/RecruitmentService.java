package com.estsoft.findmember_team01.recruitment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.estsoft.findmember_team01.exception.NotExistsIdException;


@Service
public class RecruitmentService {

	private final RecruitmentRepository recruitmentRepository;

	public RecruitmentService(RecruitmentRepository recruitmentRepository) {
		this.recruitmentRepository = recruitmentRepository;
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
			return recruitmentRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
		} else {
			return recruitmentRepository.findAll(pageable);
		}
	}

	public Page<Recruitment> getRecruitmentsByStatusAndKeyword(int page, String sortBy, int status, String keyword) {
		Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, sortBy));
		return recruitmentRepository.findByStatusAndKeyword(status, keyword, pageable);
	}

}
