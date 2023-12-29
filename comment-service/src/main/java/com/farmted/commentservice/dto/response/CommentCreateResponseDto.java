package com.farmted.commentservice.dto.response;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class CommentCreateResponseDto {

    private String content;
    private String name;
    private Date createdDate;
    private Date lastModifiedDate;

    public CommentCreateResponseDto(String content, String name, Date createdDate, Date lastModifiedDate) {

        this.content = content;
        this.name = name;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;

    }
}
