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
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}/profile")
    public String getProfilePage(@PathVariable Long id, Model model) {
        MemberDTO user = userService.getUser(id);
        model.addAttribute("user", user);
        return "profile/profile";
    }

    @PostMapping("/{id}/update")
    public String updateUserText(@PathVariable Long id, @ModelAttribute MemberDTO memberDTO) {
        userService.updateUser(id, memberDTO);
        return "redirect:/api/profile/view/" + id;
    }

    @PostMapping("/{id}/image")
    public String updateProfileImage(@PathVariable Long id,
        @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String filename = fileStorageService.store(file);
            userService.updateProfileImage(id, filename);
        }
        return "redirect:/api/profile/view/" + id;
    }

    @GetMapping("/{id}/view")
    public String getProfileViewPage(@PathVariable Long id, Model model) {
        MemberDTO user = userService.getUser(id);
        PostDTO post = userService.getLatestPostByUser(id);
        model.addAttribute("user", user);
        model.addAttribute("post", post);
        return "profile/profileView";
    }
}