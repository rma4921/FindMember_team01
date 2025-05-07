package com.estsoft.findmember_team01.information.repository;

import com.estsoft.findmember_team01.information.domain.Information;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationRepository extends JpaRepository<Information, Long> {
    List<Information> findByTitleContainingOrContentContaining(String title, String content);

}
