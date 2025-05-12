//package com.estsoft.findmember_team01.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // "/images/uploads/**" 요청을 로컬 파일시스템의 "uploads/" 디렉토리에서 찾게끔 매핑
//        registry.addResourceHandler("/images/uploads/**")
//                .addResourceLocations("file:" + uploadDir + "/");
//    }
//
//    // 기본 URL -> profile 페이지로 리다이렉트
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addRedirectViewController("/", "/profile");
//    }
//}
