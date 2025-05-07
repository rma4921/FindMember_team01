package com.estsoft.findmember_team01.admin.controller;

import com.estsoft.findmember_team01.admin.dto.UpdateLevelRequest;
import com.estsoft.findmember_team01.admin.service.adminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class adminController {

    private final adminService adminService;

    // 사용자 레벨 조정 메서드
    @PutMapping("/api/admin/users/{id}/level")
    public ResponseEntity<String> updateUserLevel(@PathVariable Long id,
        @RequestParam UpdateLevelRequest request) {
        adminService.updateUserLevel(id, request.getLevel());
        return ResponseEntity.ok("레벨 및 권한이 수정되었습니다.");
    }
}
