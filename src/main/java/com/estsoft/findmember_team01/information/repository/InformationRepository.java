package com.estsoft.findmember_team01.information.repository;

import com.estsoft.findmember_team01.information.domain.Information;
import com.estsoft.findmember_team01.information.domain.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationRepository extends JpaRepository<Information, Long> {

    // status가 null값이면 전체 keyword, status 둘다 있으면 조건에 맞게 필터링
    @Query("SELECT i FROM Information i " +
        "WHERE (:status IS NULL OR i.status = :status) " +
        "AND (LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Information> findByStatusAndKeywordPaged(
        @Param("status") Status status,
        @Param("keyword") String keyword,
        Pageable pageable
    );
}
