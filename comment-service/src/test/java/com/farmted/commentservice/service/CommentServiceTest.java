package com.farmted.commentservice.service;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.dto.request.CommentCreateRequestDto;
import com.farmted.commentservice.dto.request.CommentUpdateRequestDto;
import com.farmted.commentservice.repository.CommentRepository;
import com.farmted.commentservice.util.InitDB;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;

    @Test
    void getAllComments() {
        //given

        //when
        List<Comment> findComments = commentService.getAllComments();

        //then
        Assertions.assertThat(findComments.size()).isEqualTo(4);
    }

    @Test
    void getCommentById() {
        //given

        Long commentUuid = 1L;

        //when
        Comment findComment = commentService.getCommentById(commentUuid);

        //then
        Assertions.assertThat(findComment.getCommentMemberName()).isEqualTo("이찬혁");

    }

    @Test
    void createComment() {
//         Given
        String memberUuid = "createMemberUuid";
        String boardUuid = "creatBoardUuid";
        CommentCreateRequestDto commentDto = new CommentCreateRequestDto();
        commentDto.setContent("content");
        commentDto.setName("memberName");
        // When
        commentService.createComment(memberUuid, boardUuid, commentDto);

        // Then
        assertEquals(commentRepository.findCommentsByBoardUuid(boardUuid).size(), 1);

    }

    @Test
    @Transactional
    void updateComment() {
//         Given
        Comment updatedComment = commentRepository.findAll().get(0);
        String commentUuid = updatedComment.getCommentUuid();
        CommentUpdateRequestDto updateRequestDto = new CommentUpdateRequestDto();
        updateRequestDto.setCommentUuid(commentUuid);
        updateRequestDto.setContent("수정된 내용");
//        // When
        commentService.updateComment(updateRequestDto);

        // Then
        assertEquals(updatedComment.getCommentContent(), "수정된 내용");
    }

    @Test
    void deleteComment() {
            // Given
            Long commentIdToDelete = 1L;

            // Get the initial count of comments
            int initialCommentCount = commentService.getAllComments().size();

            // When
            System.out.println("Before deletion - Comment count: " + initialCommentCount);
            commentService.deleteComment(String.valueOf(commentIdToDelete));
            System.out.println("Comment deleted with ID: " + commentIdToDelete);

            // Then
            // Verify that the comment with the given ID is deleted
            Comment deletedComment = commentService.getCommentById(commentIdToDelete);
            Assertions.assertThat(deletedComment).isNull();
            System.out.println("Deleted comment: " + deletedComment);

            // Verify that the total number of comments is reduced by 1
            int finalCommentCount = commentService.getAllComments().size();
            Assertions.assertThat(finalCommentCount).isEqualTo(initialCommentCount - 1);
            System.out.println("After deletion - Comment count: " + finalCommentCount);
        }
    }


