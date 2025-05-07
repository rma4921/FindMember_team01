package com.estsoft.findmember_team01.admin.service;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    // 유저 레벨 수정
    public void updateUserLevel(Long memberId, int level) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        member.addExp(level);
        memberRepository.save(member);
    }
}
