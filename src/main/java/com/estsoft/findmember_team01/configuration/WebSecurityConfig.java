package com.estsoft.findmember_team01.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// @RequiredArgsConstructor
public class WebSecurityConfig {
    // private final UserAuthenticationSuccessHandler successHandler;
    
    @Bean
    public WebSecurityCustomizer configure() {
        return web -> web.ignoring()
            .requestMatchers("/static/**")
            .requestMatchers("/css/**", "/js/**", "/images/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth ->
                auth.requestMatchers("/login", "/signup", "/user", "/api/user/signup").permitAll()
                    .requestMatchers("/testurl").permitAll() // 테스트용 나중에 삭제하기!!
                    .requestMatchers("/admin").hasRole("ADMIN") // 관리자 페이지 설정
                    .anyRequest().authenticated())
            .formLogin(auth -> auth.loginPage("/login")
                // .successHandler(successHandler)
                .defaultSuccessUrl("/main"))
            .logout(auth -> auth.logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true))
            .csrf(auth -> auth.disable()); // csrf 비활성화
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
