package com.farmted.commentservice.service;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.dto.request.CommentCreateRequestDto;
import com.farmted.commentservice.dto.request.CommentUpdateRequestDto;
import com.farmted.commentservice.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {

        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {

        return commentRepository.findById(id).orElse(null);
    }
    public  void createComment(String memberUuid, String boardUuid ,CommentCreateRequestDto commentCreateRequestDto) {
        Comment comment = commentCreateRequestDto.toEntity(memberUuid, boardUuid);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(CommentUpdateRequestDto updateRequestDto) {
        Comment comment = commentRepository.findCommentByCommentUuid(updateRequestDto.getCommentUuid())
                .orElseThrow(() -> new RuntimeException("댓글이 없습니다."));

        comment.updateComment(updateRequestDto);
        }



    public void deleteComment(String uuid) {
        commentRepository.deleteCommentByCommentUuid(uuid);
    }
}
