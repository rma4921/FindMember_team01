package com.estsoft.findmember_team01.profile.controller;

import com.estsoft.findmember_team01.profile.dto.MemberDTO;
import com.estsoft.findmember_team01.profile.dto.PostDTO;
import com.estsoft.findmember_team01.profile.service.FileStorageService;
import com.estsoft.findmember_team01.profile.service.UserService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
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

    // 1) 설정 화면
    @GetMapping("/{id}")
    public String showProfile(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "profile/profile";
    }

    // 2) 텍스트 필드(닉네임·기술) 업데이트
    @PostMapping("/{id}")
    public String updateProfile(@PathVariable Long id,
        @ModelAttribute MemberDTO dto) {
        userService.updateUser(id, dto);
        return "redirect:/profile/view/" + id;
    }

    // 3) 이미지 전용 업로드
    @PostMapping("/{id}/image")
    public String uploadImage(@PathVariable Long id,
        @RequestParam("file") MultipartFile file) throws IOException {
        String filename = fileStorageService.store(file);
        userService.updateProfileImage(id, filename);
        return "redirect:/profile/view/" + id;
    }

    // 4) 조회 화면
    @GetMapping("/view/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        MemberDTO user = userService.getUser(id);
        PostDTO post = userService.getLatestPostByUser(id);
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        return "profile/profileView";
    }
}