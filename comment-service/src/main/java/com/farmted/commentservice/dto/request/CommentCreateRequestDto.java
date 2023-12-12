package com.farmted.commentservice.dto.request;

import com.farmted.commentservice.domain.Comment;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {

    private String content;
    private String name;

    private String commentUUID;
    private String userUUID;
    private String boardUUID;

    // 생성자, 게터, 세터 등 필요한 메서드들....

    public Comment toEntity(String memberUUID, String boardUUID) {
        return Comment.builder()
        .commentContent(this.content)
        .commentMemberName(this.name)
        .commentUuid(UUID.randomUUID().toString())
        .memberUuid(memberUUID)
        .boardUuid(boardUUID)
                .build();
    }
}