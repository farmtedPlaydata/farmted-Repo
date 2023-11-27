package com.farmted.commentservice.service;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getALlComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }
    public  Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long id, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(id).orElse(null);
        if(existingComment != null) {
            existingComment.setCommentContent(updatedComment.getCommentContent());
            return commentRepository.save(existingComment);
        }
        return null;
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
