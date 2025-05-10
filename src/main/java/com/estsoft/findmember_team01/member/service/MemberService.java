package com.estsoft.findmember_team01.member.service;

import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.login.dto.LoginRequest;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserDetailsService userDetailsService;

    public Page<Member> getMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    public Page<Member> getMembersWithKeyword(String keyword, Pageable pageable) {
        return memberRepository.findByKeyword(keyword, pageable);
    }

    public void save(LoginRequest dto) {
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

    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    public void experienceHandler(String email, int exp) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new GlobalException(GlobalExceptionType.EMAIL_NOT_FOUND));

        member.addExp(exp);
        member.updateRoleByLevel();
        memberRepository.save(member);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getName().equals(email)) {
            UserDetails user = userDetailsService.loadUserByUsername(email);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(user,
                auth.getCredentials(), user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }
}
