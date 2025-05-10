package com.estsoft.findmember_team01.admin.service;

import com.estsoft.findmember_team01.exception.NotExistsIdException;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    public void updateUserLevel(Long memberId, int level) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        member.addExp(level);
        memberRepository.save(member);
    }

    @Transactional
    public void changeRole(Long memberId, String role) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotExistsIdException(memberId));

        member.updateRole(role);
    }
}
