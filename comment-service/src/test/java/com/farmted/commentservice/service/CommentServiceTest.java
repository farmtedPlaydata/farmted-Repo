package com.farmted.commentservice.service;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.dto.request.CommentCreateRequestDto;
import com.farmted.commentservice.dto.request.CommentUpdateRequestDto;
import com.farmted.commentservice.repository.CommentRepository;
import com.farmted.commentservice.vo.MemberVo;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        String memberUUID = "memberUUID";
        String boardUUID = "boardUUID";
        IntStream.rangeClosed(1, 5).forEach(i->
                        commentRepository.save(
                                new CommentCreateRequestDto(
                                        "댓글작성했습니다."+i,
                                        "이찬혁",
                                        "comment"+i,
                                        "user"+i,
                                        "board"+i)
                                            .toEntity(memberUUID, boardUUID,
                                                    new MemberVo("memberName", "MemberImage")))
        );

    }

    @Test
    void getAllComments() {
        //given
        Long commentUuid = 1L;

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
        Assertions.assertThat(findComment.getMemberName()).isEqualTo("이찬혁");
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
    @Transactional
    void deleteComment() {
        // Given
        String commentUuid = commentRepository.findAll().get(0).getCommentUuid();
        // Get the initial count of comments
        int beforeCommentCount = commentService.getAllComments().size();

        // When
        commentService.deleteComment(commentUuid);

        // Then
        // Verify that the comment with the given UUID is deleted
        int afterCommentCount = commentService.getAllComments().size();
        Assertions.assertThat(afterCommentCount).isEqualTo(beforeCommentCount - 1);
//
//        // Ensure that the deleted comment is not present in the list
//        Comment deletedComment = commentService.getCommentById(commentUuid);
//        Assertions.assertThat(deletedComment).isNull();


    }
    }


