package com.farmted.commentservice.domain;

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
public class Comment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(nullable = false)
    private String commentUuid;

    @Column(name = "content")
    private String commentContent;

    // 생성 시간
    private LocalDateTime createdAt;

    // 수정 시간
    private LocalDateTime updatedAt;

    // 생성자, 게터, 세터 등 필요한 메서드들은 생략하였습니다.

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 회원 UUID
    private UUID memberUuid;

    // 게시판 UUID
    private UUID boardUuid;

    @Column(name = "board_id")
    private int commentBoardId;

    @Column(name = "member_id")
    private int commentMemberId;

    @Column(name = "member_name")
    private String commentMemberName;

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getCommentUuid() {
        return commentUuid;
    }

    public void setCommentUuid(String commentUuid) {
        this.commentUuid = commentUuid;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getMemberUuid() {
        return memberUuid;
    }

    public void setMemberUuid(UUID memberUuid) {
        this.memberUuid = memberUuid;
    }

    public UUID getBoardUuid() {
        return boardUuid;
    }

    public void setBoardUuid(UUID boardUuid) {
        this.boardUuid = boardUuid;
    }

    public int getCommentBoardId() {
        return commentBoardId;
    }

    public void setCommentBoardId(int commentBoardId) {
        this.commentBoardId = commentBoardId;
    }

    public int getCommentMemberId() {
        return commentMemberId;
    }

    public void setCommentMemberId(int commentMemberId) {
        this.commentMemberId = commentMemberId;
    }

    public String getCommentMemberName() {
        return commentMemberName;
    }

    public void setCommentMemberName(String commentMemberName) {
        this.commentMemberName = commentMemberName;
    }
}