package com.estsoft.findmember_team01.login.service;

import com.estsoft.findmember_team01.login.domain.Member;
import com.estsoft.findmember_team01.login.dto.AddMemberRequest;
import com.estsoft.findmember_team01.login.repository.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    public void save(AddMemberRequest dto) {
        System.out.println("SAVE 실행");
        String nickname = generateUniqueNickname();
        Member member = new Member(dto.getEmail(), encoder.encode(dto.getPassword()), nickname);
        member.updateRoleByLevel();
        memberRepository.save(member);
    }

    private String generateUniqueNickname() {
        String nickname;
        do {
            nickname = "user_" + UUID.randomUUID().toString().substring(0, 8);
        } while (memberRepository.existsByNickname(nickname));
        return nickname;
    }

    public void updateLoginInfo(Member member, String userAgent) {
        member.updateLoginInfo(userAgent);
        memberRepository.save(member);
    }
}
