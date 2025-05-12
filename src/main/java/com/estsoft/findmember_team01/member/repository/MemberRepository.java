package com.estsoft.findmember_team01.member.repository;

import com.estsoft.findmember_team01.member.domain.Member;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT r FROM Member r WHERE (r.nickname LIKE %:keyword%)")
    Page<Member> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<Member> findByEmail(String email);

    boolean existsByNickname(String nickname);
}