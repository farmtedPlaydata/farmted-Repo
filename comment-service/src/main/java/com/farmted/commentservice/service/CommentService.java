package com.farmted.commentservice.service;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.dto.request.CommentCreateRequestDto;
import com.farmted.commentservice.dto.request.CommentUpdateRequestDto;
import com.farmted.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> getALlComments() {

        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {

        return commentRepository.findById(id).orElse(null);
    }
    public  void createComment(String memberUuid, String boardUuid ,CommentCreateRequestDto commentCreateRequestDto) {
        Comment comment = commentCreateRequestDto.toEntity();



    }

    public void updateComment(String uuid, CommentUpdateRequestDto updateRequestDto) {
        Comment comment = commentRepository.findCommentByCommentUuid(updateRequestDto.getCommentUuid())
                .orElseThrow(() -> new RuntimeException("댓글이 없습니다."));

        comment.updateComment(updateRequestDto);
        commentRepository.save(comment);

        }



    public void deleteComment(String uuid) {
        commentRepository.deleteCommentByCommentUuid(uuid);
    }
}
