package com.estsoft.findmember_team01.application.repository;

import com.estsoft.findmember_team01.application.domain.Application;
import com.estsoft.findmember_team01.application.domain.ApplicationStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByRecruitment_RecruitmentId(Long recruitmentId);

    @Query("SELECT a FROM Application a " + "WHERE a.recruitment.recruitmentId = :recruitmentId "
        + "AND (:title IS NULL OR a.recruitment.title LIKE %:title%)")
    Page<Application> findBySearchConditions(@Param("recruitmentId") Long recruitmentId,
        @Param("title") String title, Pageable pageable);

    List<Application> findByRecruitment_RecruitmentIdAndStatus(Long recruitmentId,
        ApplicationStatus status);
}
