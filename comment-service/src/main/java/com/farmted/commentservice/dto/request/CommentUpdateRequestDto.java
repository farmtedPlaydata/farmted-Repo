package com.farmted.commentservice.dto.request;

import com.farmted.commentservice.domain.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequestDto {

    private String content;
    private String commentUuid;
}
