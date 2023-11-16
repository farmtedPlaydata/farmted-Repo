package com.farmted.boardservice.dto.response;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.enums.BoardType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseGetAuctionBoardDTO {
    // 게시글용 정보
    private String boardUuid;
    private BoardType boardType;
    private String boardTitle;
    private String boardContent;
    private long viewCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

// 요청받은 데이터로 ResponseDto 생성
    // board의 정보만 필요한거라면 Record로 바꿔도 될지도?
    // 게시글 정보
    public void assignBoard(Board board){
        this.boardUuid = board.getBoardUuID();
        this.boardType = board.getBoardType();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.viewCount = board.getViewCount();
        this.createAt = board.getCreateAt();
        this.updateAt = board.getUpdateAt();
    }

    // MS별로 따로 반환한다면 필요없는 데이터
//    // 아이템용 정보
//    private String productName;
//    private int productStock;
//        // 최저가는 경매 정보엔 필요없을 것이라 판단해서 제외
//    private String productSource;
//    private String productImage;
//        // 상품 상태의 경우, GET 요청엔 필요하다 판단해서 추가
//    private boolean productStatus;
//    // 경매용 정보
//    private int auctionPrice;
//    private String auctionBidder;
//    private String auctionBuyer;
//    private LocalDateTime auctionDeadline;
//        // 경매 상태의 경우, GET 요청엔 필요하다 판단해서 추가
//    private Boolean auctionStatus;
//
//        // 상품 정보
//    public void getProduct(ProductVo productVo){
//        this.productName = productVo.productName();
//        this.productStock = productVo.productStock();
//        this.productImage = productVo.productImage();
//        this.productSource = productVo.productSource();
//        this.productStatus = productVo.productStatus();
//    }
//        // 경매 정보
//    public void getAuction(AuctionVo auctionVo){
//        this.auctionPrice = auctionVo.auctionPrice();
//        this.auctionBidder = auctionVo.auctionBidder();
//        this.auctionBuyer = auctionVo.auctionBuyer();
//        this.auctionDeadline = auctionVo.auctionDeadline();
//        this.auctionStatus = auctionVo.auctionStatus();
//    }
}
