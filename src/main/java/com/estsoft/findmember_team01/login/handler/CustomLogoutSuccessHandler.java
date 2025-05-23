package com.estsoft.findmember_team01.login.handler;

import com.estsoft.findmember_team01.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();

            memberRepository.findByEmail(email).ifPresent(member -> {
                member.updateLogoutInfo();
                memberRepository.save(member);
            });
        }

        response.sendRedirect("/api/posts");
    }
}
