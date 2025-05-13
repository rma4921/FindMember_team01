package com.estsoft.findmember_team01.profile.service;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import com.estsoft.findmember_team01.profile.dto.MemberDTO;
import com.estsoft.findmember_team01.profile.dto.PostDTO;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements UserService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDTO getUser(Long id) {
        Member m = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        return MemberDTO.builder().id(m.getId()).email(m.getEmail()).level(m.getLevel())
            .nickname(m.getNickname()).tech(m.getTech()).profileImageUrl(m.getProfileImageUrl())
            .build();
    }

    @Override
    @Transactional
    public void updateUser(Long id, MemberDTO memberDTO) {
        Member m = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        m.updateNickname(memberDTO.getNickname());
        m.updateLevel(memberDTO.getLevel());
        m.updateTech(memberDTO.getTech());
        memberRepository.save(m);
    }

    @Override
    @Transactional
    public void updateProfileImage(Long id, String filename) {
        Member m = memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        if (m.getLevel() >= 5) {
            m.updateProfileImageUrl("/images/uploads/" + filename);
            memberRepository.save(m);
        }
    }

    @Override
    public PostDTO getLatestPostByUser(Long userId) {
        return PostDTO.builder().title("팀 프로젝트").content("멋진거 할사람 구함~")
            .lastModified(LocalDateTime.of(2025, 4, 21, 15, 13)).views(313).build();
    }
}