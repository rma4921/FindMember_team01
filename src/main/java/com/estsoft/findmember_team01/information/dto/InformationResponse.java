package com.estsoft.findmember_team01.information.dto;


import com.estsoft.findmember_team01.information.domain.Information;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//목록 상세 조회용 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InformationResponse {

    private Long informationId;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    public InformationResponse(Information information) {
        this.informationId = information.getInformationId();
        this.title = information.getTitle();
        this.content = information.getContent();
        this.createdAt = information.getCreateAt();
        this.updatedAt = information.getUpdateAt();
    }
}
