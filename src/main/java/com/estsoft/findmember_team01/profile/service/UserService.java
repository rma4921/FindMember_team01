package com.estsoft.findmember_team01.profile.service;

import com.estsoft.findmember_team01.profile.dto.MemberDTO;
import com.estsoft.findmember_team01.profile.dto.PostDTO;

// UserService.java
public interface UserService {

    /**
     * 사용자 정보 조회
     */
    MemberDTO getUser(Long id);

    /**
     * 프로필 정보만 업데이트 (이미지 제외)
     */
    void updateUser(Long id, MemberDTO memberDTO);

    /**
     * 최신 포스트 조회
     */
    PostDTO getLatestPostByUser(Long userId);

    /**
     * 이미지 전용 업데이트 (파일 저장 후 filename 을 받아 호출)
     */
    void updateProfileImage(Long id, String filename);
}