package com.farmted.boardservice.facade;

import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationDetailDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationListDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.BoardException;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.service.BoardService;
import com.farmted.boardservice.service.subService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
// Board-Service 전체의 로직을 담당
// responseDto 반환을 수행 : 컨트롤러에 반환
public class BoardFacade {
    // 사용한 서비스
    private final BoardService boardService;
    private final ProductService productService;
    private final AuctionService auctionService;
    private final MemberService memberService;
    private final NoticeService noticeService;
    private final ImageService imageService;

    @Transactional
    public String createBoard(RequestCreateBoardDto boardDto,
                            String memberUuid, RoleEnums role,
                            MultipartFile... image){
        // 게시글을 작성하기 유효한 ROLE인지 확인
        // 게스트면 불가능
        if (RoleEnums.GUEST.equals(role)) {
            throw new RoleTypeException(role, boardDto.boardType());
        }
        // 회원 UUID를 통해 회원명 받기
        String boardUuid =
                boardService.createBoard(boardDto, memberUuid,
                        memberService.getMemberInfo(memberUuid));
        // 게시글 타입에 따른 하위 도메인 서비스 세팅
        switch(boardDto.boardType()){
            // 상품 서비스에 요청이 필요한 경우 : S3 업로드 이후 Feign 요청 및 예외처리
            case SALE, AUCTION -> {
                String imageUrl = imageService.uploadImageToS3(image[0]);
                boardService.imageManager(imageUrl, boardUuid);
                productService.postProduct(boardDto.toProduct(boardUuid, imageUrl), memberUuid);
            }
            // 일반 게시글은 추가 처리 필요없음.
            case CUSTOMER_SERVICE, COMMISSION -> {}
            // 공지사항 : 권한 체크 및 예외처리
            case NOTICE -> noticeService.isAdmin(role);
            // 상품 : 조회용이기 때문에 게시글이 생성되선 안됨.
            case PRODUCT -> throw new BoardException(boardDto.boardType(), ExceptionType.SAVE);
        }
        // 이후에 보상 트랜잭션 추가 구현 필요 ( 상품저장이 성공했는데 게시글 저장 실패 )
        return boardUuid;
    }

    // 전체 게시글 카테고리별 리스트 조회
    public ResponseGetCombinationListDto getBoardList(BoardType category, int pageNo) {
        // 게시글 리스트 담기
        ResponseGetCombinationListDto combinationListDto = boardService.getBoardList(category, pageNo);

        // 상품 리스트 담기
        switch (category) {
            case PRODUCT, SALE, AUCTION
                    -> combinationListDto.setProductList(
                    productService.getProductList(category, pageNo));
        }
        // 경매 리스트 담기 - 경매 전용 카테고리일 따로 조회
        if(BoardType.AUCTION.equals(category))
            combinationListDto.setAuctionList(
                    auctionService.getAuctionList(pageNo));
        return combinationListDto;
    }

    // 작성자 글 카테고리별 리스트 조회 (판매자 입장)
    public ResponseGetCombinationListDto getWriterBoardList(BoardType category, int pageNo, String sellerUuid) {
        if (pageNo < 1) pageNo = 0;
        ResponseGetCombinationListDto combinationListDto = boardService.getWriterBoardList(category, pageNo, sellerUuid);
        //  상품 리스트 담기
        switch (category) {
            case PRODUCT, SALE, AUCTION
                    -> combinationListDto.setProductList(
                    productService.getProductListByMember(sellerUuid, category, pageNo));
        }
        // 경매 전용 카테고리인 경우엔 따로 조회
        if(BoardType.AUCTION.equals(category))
            combinationListDto.setAuctionList(
                    auctionService.getSellerAuctionList(sellerUuid, pageNo)
            );
        return combinationListDto;
    }



    // 개별 경매 상품 상세 조회
    public ResponseGetCombinationDetailDto getBoard(String boardUuid) {
        // 해당하는 게시글 가져오기
        ResponseGetCombinationDetailDto combinationDetailDto = boardService.getBoard(boardUuid);
        // 상품이 포함된 카테고리의 경우 상품 가져오기
        BoardType category = combinationDetailDto.getBoardDetail().getBoardType();
        switch(category){
            case SALE, AUCTION -> combinationDetailDto.setProductDetail(productService.getProductByBoardUuid(boardUuid));
        }
        // 경매 카테고리의 경우 가져오기
        if(BoardType.AUCTION.equals(category))
            combinationDetailDto.setAuctionDetail(auctionService.getAuctionDetail(boardUuid));
        return combinationDetailDto;
    }

    // 게시글 업데이트
    @Transactional
    public void updateBoard(RequestUpdateProductBoardDto updateDTO, String boardUuid, String memberUuid) throws InterruptedException {
        // 재시도 폭주를 막기 위한 지수 백오프
        int maxRetries = 3; // 최대 3회까지 시도
        int retries = 0;
        Boolean productChange = null;
        while (retries < maxRetries) {
            try {
                productChange = boardService.updateBoard(updateDTO, boardUuid, memberUuid);
                break;
            } catch (Exception e) {
                // 낙관적 락에 의해서 버전 정합성이 맞지 않아 예외가 발생했다면
                int sleepTime = (int) Math.pow(2, retries++) * 100;  // 지수 백오프
                Thread.sleep(sleepTime);
            }
        }
        if(retries == maxRetries)    // 재시도 횟수가 넘으면 예외
            throw new BoardException(ExceptionType.UPDATE);
        if(Boolean.TRUE.equals(productChange))
            productService.checkUpdateProduct(boardUuid, updateDTO, memberUuid);
    }

    // 게시글 삭제, 성공하면 1페이지로 리다이렉트
    @Transactional
    public void deleteBoard(String boardUuid, String uuid) {
        String imageURL = boardService.deleteBoard(boardUuid, uuid);
        // 상품이 포함된 게시글의 경우만 비활성화 요청
        if(Objects.nonNull(imageURL)) {
            imageService.deleteImage(imageURL);
            productService.checkDeleteProduct(boardUuid, uuid);
        }
    }
}
