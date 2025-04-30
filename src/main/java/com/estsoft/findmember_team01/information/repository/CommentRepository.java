package com.estsoft.findmember_team01.information.repository;

import com.estsoft.findmember_team01.information.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByInformation_InformationId(Long informationId);
}
