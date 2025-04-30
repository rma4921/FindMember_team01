package com.estsoft.findmember_team01.information.controller;


import com.estsoft.findmember_team01.information.domain.Information;
import com.estsoft.findmember_team01.information.dto.InformationRequest;
import com.estsoft.findmember_team01.information.dto.InformationResponse;
import com.estsoft.findmember_team01.information.service.InformationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InformationController {

    private final InformationService informationService;

    public InformationController(InformationService informationService) {
        this.informationService = informationService;
    }

    //목록 조회
    @GetMapping("/api/information")
    public ResponseEntity<List<InformationResponse>> findAllInformation() {
        List<Information> informations = informationService.findAllInformation();

        List<InformationResponse> responseBody = informations.stream()
            .map(InformationResponse::new)
            .toList();
        return ResponseEntity.ok(responseBody);
    }

    // 상세 조회
    @GetMapping("/api/information/{id}")
    public ResponseEntity<InformationResponse> findInformationById(@PathVariable("id") Long id) {
        Information information = informationService.findInformationById(id);
        return ResponseEntity.ok(information.toDto());
    }

    // 글 작성
    @PostMapping("/api/information")
    public ResponseEntity<InformationResponse> addInformation(
        @RequestBody InformationRequest request) {
        Information saveInformation = informationService.addInformation(request);

        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(saveInformation.toDto());
    }

    //게시글 수정
    @PutMapping("/api/information/{id}")
    public ResponseEntity<InformationResponse> updateInformation(@PathVariable("id") Long id,
        @RequestBody InformationRequest request) {

        Information updatedInformation = informationService.updateInformation(id, request);
        return ResponseEntity.ok(updatedInformation.toDto());

    }

    //게시글 삭제
    @DeleteMapping("/api/information/{id}")
    public ResponseEntity<Void> deleteInformation(@PathVariable("id") Long id) {
        informationService.deleteInformation(id);
        return ResponseEntity.ok().build();

    }

}
