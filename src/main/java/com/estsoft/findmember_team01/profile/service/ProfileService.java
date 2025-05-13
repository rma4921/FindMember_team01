package com.estsoft.findmember_team01.profile.service;

import com.estsoft.findmember_team01.profile.dto.MemberDTO;
import com.estsoft.findmember_team01.profile.dto.PostDTO;

public interface ProfileService {

    MemberDTO getUser(Long id);

    void updateUser(Long id, MemberDTO memberDTO);

    PostDTO getLatestPostByUser(Long userId);

    void updateProfileImage(Long id, String filename);
}