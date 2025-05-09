package com.estsoft.findmember_team01.report.repository;

import com.estsoft.findmember_team01.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
