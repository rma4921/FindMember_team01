package com.estsoft.findmember_team01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FindMemberTeam01Application {

    public static void main(String[] args) {
        SpringApplication.run(FindMemberTeam01Application.class, args);
        
    }
}
