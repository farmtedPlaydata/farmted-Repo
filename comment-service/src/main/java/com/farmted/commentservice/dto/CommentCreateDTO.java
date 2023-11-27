package com.farmted.commentservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateDTO {

    private Long commentId;
    private String content;
    private String name;
    private Date createdDate;
    private Date lastModifiedDate;

    private UUID commentUUID;
    private UUID userUUID;
    private UUID boardUUID;

    // 생성자, 게터, 세터 등 필요한 메서드들....

    public CommentCreateDTO(UUID commentUUID, String content, String name, Date createdDate, Date lastModifiedDate, UUID userUUID, UUID boardUUID) {
        this.commentUUID = commentUUID;
        this.content = content;
        this.name = name;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.userUUID = userUUID;
        this.boardUUID = boardUUID;
    }
}