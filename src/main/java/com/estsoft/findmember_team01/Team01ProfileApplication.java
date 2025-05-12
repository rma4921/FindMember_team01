package com.estsoft.findmember_team01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Team01ProfileApplication {
    public static void main(String[] args) {
        SpringApplication.run(Team01ProfileApplication.class, args);
    }
}