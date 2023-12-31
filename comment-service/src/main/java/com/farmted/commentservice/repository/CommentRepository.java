package com.farmted.commentservice.repository;


import com.farmted.commentservice.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findCommentByCommentUuid(String commentUuid);
    List<Comment> findCommentsByBoardUuid(String boardUuid);
    void deleteCommentByCommentUuid(String uuid);

}
