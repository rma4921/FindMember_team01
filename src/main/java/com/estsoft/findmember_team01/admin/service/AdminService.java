package com.estsoft.findmember_team01.admin.service;

import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
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
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.MEMBER_NOT_FOUND));

        member.addExp(level);
        memberRepository.save(member);
    }

    @Transactional
    public void changeRole(Long memberId, String role) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.MEMBER_NOT_FOUND));

        if (role.equals("ADMIN")) {
            member.updateLevel(0L);
        } else if (role.equals("MASTER")) {
            member.updateLevel(7L);
        } else {
            member.updateLevel(1L);
        }

        member.updateRole(role);
    }
}
