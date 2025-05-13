package com.estsoft.findmember_team01.profile.controller;

import com.estsoft.findmember_team01.exception.GlobalException;
import com.estsoft.findmember_team01.exception.type.GlobalExceptionType;
import com.estsoft.findmember_team01.member.domain.Member;
import com.estsoft.findmember_team01.profile.dto.MemberDTO;
import com.estsoft.findmember_team01.profile.dto.PostDTO;
import com.estsoft.findmember_team01.profile.service.FileStorageService;
import com.estsoft.findmember_team01.profile.service.ProfileService;
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
@RequestMapping("/api/profile")
public class ProfilePageController {

    private final ProfileService profileService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public String showProfile(@PathVariable Long id, @AuthenticationPrincipal Member loginMember,
        Model model) {

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (!id.equals(loginMember.getId())) {
            throw new GlobalException(GlobalExceptionType.CANNOT_ACCESS);
        }

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("member", profileService.getUser(id));
        return "profile/profile";
    }

    @PostMapping("/{id}")
    public String updateProfile(@PathVariable Long id, @AuthenticationPrincipal Member loginMember,
        @ModelAttribute MemberDTO dto) {
        if (loginMember == null || !id.equals(loginMember.getId())) {
            throw new GlobalException(GlobalExceptionType.CANNOT_ACCESS);
        }

        profileService.updateUser(id, dto);

        return "redirect:/api/profile/view/" + id;
    }

    @PostMapping("/{id}/image")
    public String uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file)
        throws IOException {
        String filename = fileStorageService.store(file);
        profileService.updateProfileImage(id, filename);
        return "redirect:/api/profile/view/" + id;
    }

    @GetMapping("/view/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {

        MemberDTO member = profileService.getUser(id);
        PostDTO post = profileService.getLatestPostByUser(id);
        model.addAttribute("member", member);
        model.addAttribute("post", post);

        return "profile/profileView";
    }
}