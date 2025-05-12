package com.estsoft.findmember_team01.profile.repository;

import com.estsoft.findmember_team01.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Long> {

}