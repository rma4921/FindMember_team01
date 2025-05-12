package com.estsoft.findmember_team01.profile.controller;

import com.estsoft.findmember_team01.profile.dto.PostDTO;
import com.estsoft.findmember_team01.profile.dto.MemberDTO;
import com.estsoft.findmember_team01.profile.service.FileStorageService;
import com.estsoft.findmember_team01.profile.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    /** 프로필 설정 화면 */
    @GetMapping("/{id}/profile")
    public String getProfilePage(@PathVariable Long id, Model model) {
        MemberDTO user = userService.getUser(id);
        model.addAttribute("user", user);
        return "profile";
    }

    /** 이름/닉네임/이메일/기술스택만 업데이트 */
    @PostMapping("/{id}/update")
    public String updateUserText(
            @PathVariable Long id,
            @ModelAttribute MemberDTO memberDTO
    ) {
        userService.updateUser(id, memberDTO);
        return "redirect:/profile/view/" + id;
    }

    /** 프로필 이미지 전용 업로드 (레벨 5 이상만 반영) */
    @PostMapping("/{id}/image")
    public String updateProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (file != null && !file.isEmpty()) {
            // 1) 물리 파일 저장
            String filename = fileStorageService.store(file);
            // 2) DB에 URL만 업데이트 (서비스에서 레벨 체크)
            userService.updateProfileImage(id, filename);
        }
        return "redirect:/profile/view/" + id;
    }

    /** 프로필 조회 화면 */
    @GetMapping("/{id}/view")
    public String getProfileViewPage(
            @PathVariable Long id,
            Model model
    ) {
        MemberDTO user = userService.getUser(id);
        PostDTO post = userService.getLatestPostByUser(id);
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        return "profileView";
    }
}