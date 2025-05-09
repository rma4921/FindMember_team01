package com.estsoft.findmember_team01.information.service;

import com.estsoft.findmember_team01.information.domain.Information;
import com.estsoft.findmember_team01.information.domain.Status;
import com.estsoft.findmember_team01.information.dto.InformationRequest;
import com.estsoft.findmember_team01.information.repository.InformationRepository;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InformationService {

    private final MemberRepository memberRepository;
    private final InformationRepository informationRepository;

    public InformationService(InformationRepository informationRepository, MemberRepository memberRepository) {
        this.informationRepository = informationRepository;
        this.memberRepository = memberRepository;
    }

    public List<Information> findAllInformation() {
        return informationRepository.findAll(Sort.by(Direction.DESC, "createAt"));
    }

    public Information findInformationById(Long id) {
        return informationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not exist information id: " + id));
    }

    public Information addInformation(InformationRequest request) {
        var member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new RuntimeException("Member not found"));

        var information = Information.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .member(member)
            .build();

        return informationRepository.save(information);
    }

    @Transactional
    public Information updateInformation(Long id, InformationRequest request) {
        var information = informationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not exist information id: " + id));
        information.updateInformation(request.getTitle(), request.getContent());
        return information;
    }

    public void deleteInformation(Long id) {
        informationRepository.deleteById(id);
    }

    public Page<Information> findAllPaged(Pageable pageable) {
        return informationRepository.findAll(pageable);
    }

    public Page<Information> searchByStatusAndKeywordPaged(Status status, String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) keyword = "";
        return informationRepository.findByStatusAndKeywordPaged(status, keyword, pageable);
    }

    @Transactional
    public void markAsSolved(Long id, Long memberId) {
        var info = informationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("정보가 없습니다."));
        if (!info.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("작성자만 게시글을 해결 처리할 수 있습니다.");
        }
        info.setStatus(Status.SOLVED);
    }
}
