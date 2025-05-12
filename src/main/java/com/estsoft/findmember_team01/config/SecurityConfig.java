//package com.estsoft.findmember_team01.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/profile", "/css/**", "/js/**", "/images/**", "/api/**").permitAll()
//                        .anyRequest().permitAll()
//                )
//                .formLogin(form -> form.disable()); // 기본 로그인폼 제거
//
//        return http.build();
//    }
//}