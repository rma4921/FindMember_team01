package com.estsoft.findmember_team01.configuration;

import com.estsoft.findmember_team01.member.handler.CustomLogoutSuccessHandler;
import com.estsoft.findmember_team01.member.handler.UserAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final UserAuthenticationSuccessHandler loginSuccessHandler;

    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring().requestMatchers("/static/**")
            .requestMatchers("/css/**", "/js/**", "/images/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
            auth -> auth.requestMatchers("/login", "/signup", "/user").permitAll()
                .requestMatchers("/api/user/signup", "/logout").permitAll()
                .requestMatchers("/api/posts", "/api/posts/**").permitAll()
                .requestMatchers("/information/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/user/*").authenticated()
                .requestMatchers("/mypage").authenticated() // 테스트용 mypage
                .requestMatchers("/api/user/exp").permitAll()
                .requestMatchers("/api/admin", "/api/admin/**").hasRole("ADMIN").anyRequest()
                .authenticated()).formLogin(
            auth -> auth.loginPage("/login").usernameParameter("email")
                .passwordParameter("password").failureUrl("/login?error=true")
                .successHandler(loginSuccessHandler)).logout(
            auth -> auth.logoutSuccessHandler(logoutSuccessHandler).invalidateHttpSession(true)
                .clearAuthentication(true)).csrf(auth -> auth.disable());
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}