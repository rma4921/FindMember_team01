package com.estsoft.findmember_team01.configuration;

import com.estsoft.findmember_team01.login.handler.CustomLogoutSuccessHandler;
import com.estsoft.findmember_team01.login.handler.UserAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {

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
                .requestMatchers("/profile/**").authenticated()
                .requestMatchers("/api/posts", "/api/posts/**").permitAll()
                .requestMatchers("/information/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/user/*").authenticated()
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/uploads/**")
            .addResourceLocations("file:" + uploadDir + "/");
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/profile");
    }
}