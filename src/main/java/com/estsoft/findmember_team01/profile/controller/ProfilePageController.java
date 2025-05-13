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
@RequestMapping("/profile")
public class ProfilePageController {

    private final ProfileService profileService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public String showProfile(@PathVariable Long id, Model model) {
        model.addAttribute("member", profileService.getUser(id));
        return "profile/profile";
    }

    @PostMapping("/{id}")
    public String updateProfile(@PathVariable Long id, @ModelAttribute MemberDTO dto) {
        profileService.updateUser(id, dto);
        return "redirect:/profile/view/" + id;
    }

    @PostMapping("/{id}/image")
    public String uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file)
        throws IOException {
        String filename = fileStorageService.store(file);
        profileService.updateProfileImage(id, filename);
        return "redirect:/profile/view/" + id;
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