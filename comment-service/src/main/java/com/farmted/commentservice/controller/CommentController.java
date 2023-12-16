package com.farmted.commentservice.controller;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.dto.request.CommentCreateRequestDto;
import com.farmted.commentservice.dto.request.CommentUpdateRequestDto;
import com.farmted.commentservice.feignclient.MemberFeignClient;
import com.farmted.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment-service")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {

        commentService.getCommentById(id);
        return ResponseEntity.ok(HttpStatus.OK);
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

    @PutMapping("/{uuid}")
    public ResponseEntity<?> updateComment(@RequestBody CommentUpdateRequestDto updateRequestDto) {
        commentService.updateComment(updateRequestDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public void deleteComment(@PathVariable String uuid) {
        commentService.deleteComment(uuid);
    }
}
