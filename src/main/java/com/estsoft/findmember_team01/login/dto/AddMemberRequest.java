package com.estsoft.findmember_team01.login.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {

    private String email;
    private String password;
}