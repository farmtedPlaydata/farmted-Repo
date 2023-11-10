package com.farmted.boardservice.dto.response;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.vo.AuctionVo;
import com.farmted.boardservice.vo.ProductVo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseGetAuctionBoardDto {
    // 게시글용 정보
    private BoardType boardType;
    private String boardTitle;
    private String boardContent;
    private long viewCount;
    // 아이템용 정보
    private String productName;
    private int productStock;
        // 최저가는 경매 정보엔 필요없을 것이라 판단해서 제외
    private String productSource;
    private String productImage;
        // 상품 상태의 경우, GET 요청엔 필요하다 판단해서 추가
    private boolean productStatus;
    // 경매용 정보
    private int auctionPrice;
    private String auctionBidder;
    private String auctionBuyer;
    private LocalDateTime auctionDeadline;
        // 경매 상태의 경우, GET 요청엔 필요하다 판단해서 추가
    private Boolean auctionStatus;

    // 요청받은 데이터로 ResponseDto 생성
        // 게시글 정보
    public void getBoard(Board board){
        this.boardType = board.getBoardType();
        this.boardTitle = board.getBoardTitle();
        this.boardContent = board.getBoardContent();
        this.viewCount = board.getViewCount();
    }
        // 상품 정보
    public void getProduct(ProductVo productVo){
        this.productName = productVo.productName();
        this.productStock = productVo.productStock();
        this.productImage = productVo.productImage();
        this.productSource = productVo.productSource();
        this.productStatus = productVo.productStatus();
    }
        // 경매 정보
    public void getAuction(AuctionVo auctionVo){
        this.auctionPrice = auctionVo.auctionPrice();
        this.auctionBidder = auctionVo.auctionBidder();
        this.auctionBuyer = auctionVo.auctionBuyer();
        this.auctionDeadline = auctionVo.auctionDeadline();
        this.auctionStatus = auctionVo.auctionStatus();
    }
}
