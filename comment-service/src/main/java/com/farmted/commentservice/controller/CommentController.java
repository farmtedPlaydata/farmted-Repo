package com.farmted.commentservice.controller;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.dto.request.CommentCreateRequestDto;
import com.farmted.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment_service")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getALlComments();
    }

    @GetMapping
    public Comment getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @PostMapping("/comments/{board_uuid}")
    public ResponseEntity<?> createComment(
            @RequestHeader("UUID") String memberUuid,
            @PathVariable(value="board_uuid") String boardUuid,
            @RequestBody CommentCreateRequestDto commentDto) {
            commentService.createComment(memberUuid,
                    boardUuid, commentDto);
            return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        return commentService.updateComment(id, updatedComment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
