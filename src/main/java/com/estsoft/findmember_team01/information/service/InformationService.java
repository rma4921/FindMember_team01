package com.estsoft.findmember_team01.information.service;


import com.estsoft.findmember_team01.information.domain.Information;
import com.estsoft.findmember_team01.information.domain.Member;
import com.estsoft.findmember_team01.information.dto.InformationRequest;
import com.estsoft.findmember_team01.information.dto.InformationResponse;
import com.estsoft.findmember_team01.information.repository.CommentRepository;
import com.estsoft.findmember_team01.information.repository.InformationRepository;
import com.estsoft.findmember_team01.information.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InformationService {

    private final MemberRepository memberRepository;
    private InformationRepository informationRepository;
    private CommentRepository commentRepository;

    public InformationService(InformationRepository informationRepository,
        CommentRepository commentRepository, MemberRepository memberRepository) {
        this.informationRepository = informationRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }

    //전체 조회
    public List<Information> findAllInformation() {
        return informationRepository.findAll();
    }

    //상세 조회
    public Information findInformationById(Long id) {
        return informationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not exist information id" + id));
    }

    //글 작성
    public Information addInformation(InformationRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
            .orElseThrow(() -> new RuntimeException("Member not found"));

        Information information = Information.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .member(member)
            .build();

        return informationRepository.save(information);

    }

    //글 수정
    @Transactional
    public Information updateInformation(Long id, InformationRequest request) {

        Information information = informationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not exist information id" + id));

        information.updateInformation(request.getTitle(), request.getContent());

        return information;
    }

    public void deleteInformation(Long id) {
        informationRepository.deleteById(id);
    }
}
