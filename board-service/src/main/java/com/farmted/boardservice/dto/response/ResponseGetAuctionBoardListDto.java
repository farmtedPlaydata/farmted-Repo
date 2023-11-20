package com.farmted.boardservice.dto.response;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.vo.AuctionVo;
import com.farmted.boardservice.vo.ProductVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class ResponseGetAuctionBoardListDto {
    // 게시글용 정보
    private String boardUuid;
    private BoardType boardType;
    private String boardTitle;
    private long viewCount;
    private LocalDateTime createAt;

    public ResponseGetAuctionBoardListDto(Board board) {
        this.boardUuid = board.getBoardUuID();
        this.boardType = board.getBoardType();
        this.boardTitle = board.getBoardTitle();
        this.viewCount = board.getViewCount();
        this.createAt = board.getCreateAt();
    }

    // 상품용 정보
    private String productName;
    private String productSource;
    private String productImage;

    public void addProduct(ProductVo productVo){
        this.productName = productVo.productName();
        this.productSource = productVo.productSource();
        this.productImage = productVo.productImage();
    }

    // 경매용 정보
    private int auctionPrice;
    private LocalDateTime auctionDeadline;

    public void addAuction(AuctionVo auctionVo){
        this.auctionPrice = auctionVo.auctionPrice();
        this.auctionDeadline = auctionVo.auctionDeadline();
    }
}
