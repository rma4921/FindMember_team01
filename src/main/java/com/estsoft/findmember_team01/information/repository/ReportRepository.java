package com.estsoft.findmember_team01.information.repository;

import com.estsoft.findmember_team01.information.domain.Report;
import com.estsoft.findmember_team01.information.domain.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByMemberIdAndTargetTypeAndTargetId(Long memberId, TargetType targetType, Long targetId);
}
