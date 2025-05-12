package com.estsoft.findmember_team01.comment.repository;

import com.estsoft.findmember_team01.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByInformation_InformationId(Long informationId);
}
