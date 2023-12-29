package com.farmted.commentservice.service;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.dto.request.CommentCreateRequestDto;
import com.farmted.commentservice.dto.request.CommentUpdateRequestDto;
import com.farmted.commentservice.feignclient.MemberFeignClient;
import com.farmted.commentservice.repository.CommentRepository;
import com.farmted.commentservice.util.GlobalResponseDto;
import com.farmted.commentservice.vo.MemberVo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberFeignClient memberFeignClient;

    public List<Comment> getAllComments() {

        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {

        return commentRepository.findById(id).orElse(null);
    }
    public  void createComment(String memberUuid, String boardUuid ,CommentCreateRequestDto commentCreateRequestDto) {
        ResponseEntity<GlobalResponseDto<MemberVo>> memberResponse = memberFeignClient.memberNameAndImage(memberUuid);
        MemberVo memberInfo = memberResponse.getBody().getData();
        Comment comment = commentCreateRequestDto.toEntity(memberUuid, boardUuid, memberInfo);
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
