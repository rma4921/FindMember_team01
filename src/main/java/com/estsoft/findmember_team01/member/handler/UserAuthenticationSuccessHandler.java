package com.estsoft.findmember_team01.member.handler;

import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        String userAgent = request.getHeader("User-Agent");

        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(email + "에 해당하는 사용자 정보를 찾을 수 없습니다."));
        member.updateLoginInfo(userAgent);
        memberRepository.save(member);

        request.getSession().setAttribute("memberId", member.getId());
        request.getSession().setAttribute("memberLevel", member.getLevel());
        request.getSession().setAttribute("memberNickname", member.getNickname());

        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        String redirectUrl = (String) request.getSession().getAttribute("prevPage");
        request.getSession().removeAttribute("prevPage");

        if (isAdmin) {
            response.sendRedirect("/api/admin/users");
        } else if (redirectUrl != null) {
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("/api/posts");
        }
    }
}
