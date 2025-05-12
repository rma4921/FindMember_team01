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

    @Query("SELECT i FROM Information i " + "WHERE (:status IS NULL OR i.status = :status) "
        + "AND (LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
        + "OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Information> findByStatusAndKeywordPaged(@Param("status") Status status,
        @Param("keyword") String keyword, Pageable pageable);

    Page<Information> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);

    Page<Information> findByHideStatusFalse(Pageable pageable);

    @Query("SELECT i FROM Information i " +
        "WHERE i.hideStatus = false " +
        "AND (:status IS NULL OR i.status = :status) " +
        "AND (LOWER(i.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
        "OR LOWER(i.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Information> findVisibleByStatusAndKeyword(@Param("status") Status status,
        @Param("keyword") String keyword,
        Pageable pageable);
}
