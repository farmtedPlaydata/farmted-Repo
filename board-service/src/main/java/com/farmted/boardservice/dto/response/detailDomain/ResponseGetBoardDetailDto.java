package com.farmted.boardservice.dto.response.detailDomain;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class ResponseGetBoardDetailDto {
    // 게시글용 정보
    private String boardUuid;
    private BoardType boardType;
    private String boardTitle;
    private String boardContent;
    private long viewCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public ResponseGetBoardDetailDto(Board board){
        this.boardUuid = board.getBoardUuID();
        this.boardType = board.getBoardType();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.viewCount = board.getViewCount();
        this.createAt = board.getCreateAt();
        this.updateAt = board.getUpdateAt();
    }
}
