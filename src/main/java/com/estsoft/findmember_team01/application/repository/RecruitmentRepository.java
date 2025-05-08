package com.estsoft.findmember_team01.application.repository;

import com.estsoft.findmember_team01.application.domain.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    Page<Recruitment> findByStatus(Boolean status, Pageable pageable);

    Page<Recruitment> findByTitleContainingOrContentContaining(
        String title, String content, Pageable pageable);

    @Query("SELECT r FROM Recruitment r WHERE (r.title LIKE %:keyword% OR r.content LIKE %:keyword%)")
    Page<Recruitment> findByKeyword(
        @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM Recruitment r WHERE r.status = :status AND (r.title LIKE %:keyword% OR r.content LIKE %:keyword%)")
    Page<Recruitment> findByStatusAndKeyword(
        @Param("status") int status, @Param("keyword") String keyword,
        Pageable pageable);
}
