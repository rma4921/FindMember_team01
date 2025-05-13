package com.estsoft.findmember_team01.profile.controller;

import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.profile.dto.MemberDTO;
import com.estsoft.findmember_team01.profile.dto.PostDTO;
import com.estsoft.findmember_team01.profile.service.FileStorageService;
import com.estsoft.findmember_team01.profile.service.UserService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfilePageController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public String showProfile(@PathVariable Long id, @AuthenticationPrincipal Member loginMember, Model model) {

        // 로그인 안 했을 경우 로그인 페이지로 리다이렉트
        if (loginMember == null) {
            return "redirect:/login";
        }

        // 본인이 아닌 다른 사람의 프로필 접근 시 차단
        if (!id.equals(loginMember.getId())) {
            throw new GlobalException(GlobalExceptionType.CANNOT_ACCESS);
            // 또는 return "redirect:/access-denied";
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("user", userService.getUser(id));
        return "profile/profile";
    }

    @PostMapping("/{id}")
    public String updateProfile(@PathVariable Long id, @AuthenticationPrincipal Member loginMember, @ModelAttribute MemberDTO dto) {
        if (loginMember == null || !id.equals(loginMember.getId())) {
            throw new GlobalException(GlobalExceptionType.CANNOT_ACCESS);
        }

        userService.updateUser(id, dto);
        return "redirect:/user/" + id;
    }

    @PostMapping("/{id}/image")
    public String uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file)
        throws IOException {
        String filename = fileStorageService.store(file);
        userService.updateProfileImage(id, filename);
        return "redirect:/profile/view/" + id;
    }

    @GetMapping("/view/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        MemberDTO user = userService.getUser(id);
        PostDTO post = userService.getLatestPostByUser(id);
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        return "profile/profileView";
    }


}