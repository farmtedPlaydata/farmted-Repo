package com.farmted.boardservice.dto.response;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.enums.BoardType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class ResponseGetAuctionBoardListDTO {
    // 게시글용 정보
    private String boardUuid;
    private BoardType boardType;
    private String boardTitle;
    private long viewCount;
    private LocalDateTime createAt;

    public ResponseGetAuctionBoardListDTO(Board board) {
        this.boardUuid = board.getBoardUuID();
        this.boardType = board.getBoardType();
        this.boardTitle = board.getBoardTitle();
        this.viewCount = board.getViewCount();
        this.createAt = board.getCreateAt();
    }
}
