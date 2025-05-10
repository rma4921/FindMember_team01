package com.estsoft.findmember_team01.report.repository;

import com.estsoft.findmember_team01.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {


    Page<Report> findByStatus(Boolean status, Pageable pageable);

    Page<Report> findByReasonContaining(String content, Pageable pageable);

    @Query("SELECT r FROM Report r WHERE (r.reason LIKE %:keyword%)")
    Page<Report> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT r FROM Report r WHERE r.status = :status AND (r.reason LIKE %:keyword%)")
    Page<Report> findByStatusAndKeyword(@Param("status") int status,
        @Param("keyword") String keyword, Pageable pageable);
}
