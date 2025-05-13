package com.estsoft.findmember_team01.profile.controller;

import com.estsoft.findmember_team01.profile.dto.MemberDTO;
import com.estsoft.findmember_team01.profile.dto.PostDTO;
import com.estsoft.findmember_team01.profile.service.FileStorageService;
import com.estsoft.findmember_team01.profile.service.ProfileService;
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
@RequestMapping("/api/member")
public class ProfileController {

    private final ProfileService profileService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}/profile")
    public String getProfilePage(@PathVariable Long id, Model model) {
        MemberDTO member = profileService.getUser(id);
        model.addAttribute("member", member);
        return "profile/profile";
    }

    @PostMapping("/{id}/update")
    public String updateUserText(@PathVariable Long id, @ModelAttribute MemberDTO memberDTO) {
        profileService.updateUser(id, memberDTO);
        return "redirect:/api/profile/view/" + id;
    }

    @PostMapping("/{id}/image")
    public String updateProfileImage(@PathVariable Long id,
        @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filename = fileStorageService.store(file);
            profileService.updateProfileImage(id, filename);
        }
        return "redirect:/api/profile/view/" + id;
    }

    @GetMapping("/{id}/view")
    public String getProfileViewPage(@PathVariable Long id, Model model) {
        MemberDTO member = profileService.getUser(id);
        PostDTO post = profileService.getLatestPostByUser(id);
        model.addAttribute("member", member);
        model.addAttribute("post", post);
        return "profile/profileView";
    }
}