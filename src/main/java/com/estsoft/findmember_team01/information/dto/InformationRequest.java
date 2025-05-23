package com.estsoft.findmember_team01.information.dto;


import com.estsoft.findmember_team01.information.domain.Information;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InformationRequest {

    private String title;
    private String content;
    private Long memberId;

    public Information toEntity() {
        return Information.builder().title(title).content(content).build();
    }
}
