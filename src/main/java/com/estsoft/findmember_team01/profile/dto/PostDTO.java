package com.estsoft.findmember_team01.profile.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private String title;
    private String content;
    private LocalDateTime lastModified;
    private int views;
}