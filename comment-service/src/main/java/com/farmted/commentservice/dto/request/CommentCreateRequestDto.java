package com.farmted.commentservice.dto.request;

import com.farmted.commentservice.domain.Comment;
import com.farmted.commentservice.vo.MemberVo;
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

    public Comment toEntity(String memberUUID, String boardUUID, MemberVo memberVo) {
        return Comment.builder()
        .commentContent(this.content)
        .commentUuid(UUID.randomUUID().toString())
        .memberName(this.name)
        .memberUuid(memberUUID)
        .boardUuid(boardUUID)
//        .memberName(memberVo.memberName())
        .memberImage(memberVo.memberImage())
                .build();
    }
}