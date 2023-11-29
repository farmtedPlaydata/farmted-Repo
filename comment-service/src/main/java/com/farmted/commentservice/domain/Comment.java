package com.farmted.commentservice.domain;

import com.farmted.commentservice.dto.request.CommentUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(nullable = false)
    private String commentUuid;

    @Column(name = "content")
    private String commentContent;

    // 회원 UUID
    private String memberUuid;

    // 게시판 UUID
    private String boardUuid;


    @Column(name = "member_name")
    private String commentMemberName;

    public void updateComment(CommentUpdateRequestDto dto) {
        this.commentContent = dto.getContent();
        this.commentUuid = dto.getCommentUuid();
    }
}