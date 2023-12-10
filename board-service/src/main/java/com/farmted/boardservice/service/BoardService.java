package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationDetailDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationListDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.BoardException;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.service.subService.AuctionService;
import com.farmted.boardservice.service.subService.MemberService;
import com.farmted.boardservice.service.subService.ImageService;
import com.farmted.boardservice.service.subService.NoticeService;
import com.farmted.boardservice.service.subService.ProductService;
import com.farmted.boardservice.util.Board1PageCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
// Board-Service 전체의 로직을 담당
// 1. Board-Repository 관리 : 트랜잭션 처리
// 2. responseDto 반환을 수행 : 컨트롤러에 반환
public class BoardService {
    // 레포지토리
    private final BoardRepository boardRepository;
    // 1페이징 캐시 : 카테고리가 PRODUCT(SALE+AUCTION)인 경우의 1페이지
    private final Board1PageCache board1PageCache;
    // 서브 서비스 : Feign 통신의 결과, 예외처리 담당
    private final ProductService productService;
    private final AuctionService auctionService;
    private final MemberService memberService;
    private final NoticeService noticeService;
    private final ImageService imageService;

// 게시글 카테고리별 등록
    @Transactional
    public void createBoard(RequestCreateBoardDto boardDto,
                            String uuid, RoleEnums role,
                            MultipartFile... image) {
    // 게시글을 작성하기 유효한 ROLE인지 확인
        // 게스트면 불가능
        if (RoleEnums.GUEST.equals(role)) {
            throw new RoleTypeException(role, boardDto.boardType());
        }
    // 게시글 Entity 생성 - 저장
                                                // 회원 UUID를 통해 회원명 받기
        Board board = boardDto.toBoard(uuid, memberService.getMemberInfo(uuid));
        boardRepository.save(board);
    // 게시글 타입에 따른 하위 도메인 서비스 세팅
        switch(boardDto.boardType()){
            // 상품 서비스에 요청이 필요한 경우 : S3 업로드 이후 Feign 요청 및 예외처리
            case SALE, AUCTION -> {
                String imageUrl = imageService.uploadImageToS3(image[0]);
                productService.postProduct(boardDto.toProduct(board.getBoardUuid(), imageUrl), uuid);
            }
            // 일반 게시글은 추가 처리 필요없음.
            case CUSTOMER_SERVICE, COMMISSION -> {}
            // 공지사항 : 권한 체크 및 예외처리
            case NOTICE -> noticeService.isAdmin(role);
            // 상품 : 조회용이기 때문에 게시글이 생성되선 안됨.
            case PRODUCT -> throw new BoardException(boardDto.boardType(), ExceptionType.SAVE);
        }

    // 이후에 보상 트랜잭션 추가 구현 필요 ( 상품저장이 성공했는데 게시글 저장 실패 )
    }

// 전체 게시글 카테고리별 리스트 조회
    public ResponseGetCombinationListDto getBoardList(BoardType category, int pageNo) {
        ResponseGetCombinationListDto combinationListDto = new ResponseGetCombinationListDto();
    // 게시글 리스트 담기
        combinationListDto.setBoardList(
                // 1페이지 캐싱처리 : 1페이지 && 상품 카테고리인 경우만
                (pageNo < 1 && category.equals(BoardType.PRODUCT))
                    //1페이지 캐싱
                    ? board1PageCache.getPage1()
                    // 생성일을 기준으로 내림치순 (최신 글이 먼저 조회)
                    : boardRepository.findByBoardType(category,
                        PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createdAt")))
        );
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
        ResponseGetCombinationListDto combinationListDto = new ResponseGetCombinationListDto();
        // 게시글 리스트 담기
        combinationListDto.setBoardList(
                boardRepository
                        .findByMemberUuidAndBoardType(sellerUuid, category,
                                PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createdAt")))
        );
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
        ResponseGetCombinationDetailDto combinationDetailDto = new ResponseGetCombinationDetailDto();
        // 해당하는 게시글 가져오기
        combinationDetailDto.setBoardDetail(boardRepository.findDetailByBoardUuid(boardUuid)
                .orElseThrow(() -> new BoardException(ExceptionType.GET)));

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
    public void updateBoard(RequestUpdateProductBoardDto updateDTO, String boardUuid, String uuid) {
        Board updateBoard = boardRepository.findByBoardUuidAndBoardStatusTrue(boardUuid)
                .orElseThrow(()->new BoardException(ExceptionType.UPDATE));
        // 카테고리 변경의 경우, 판매 <-> 경매(종료된)만 가능
        // 상품이 포함된 게시글의 경우만 수정 요청
        switch (updateBoard.getBoardType()){
            case SALE, AUCTION -> {
                if (Objects.requireNonNull(updateDTO.boardType()) == BoardType.SALE || updateDTO.boardType() == BoardType.AUCTION) {
                    updateBoard.updateBoardInfo(updateDTO);
                    productService.checkUpdateProduct(boardUuid, updateDTO, uuid);
                }
            }
            // 기존 게시글의 경우 타입 변경 불가능
            case NOTICE, COMMISSION, CUSTOMER_SERVICE -> {
                if(updateBoard.getBoardType().equals(updateDTO.boardType()))
                    updateBoard.updateBoardInfo(updateDTO);
                 else
                     throw new BoardException(updateBoard.getBoardType(), ExceptionType.UPDATE);

            }
            case PRODUCT -> throw new BoardException(BoardType.PRODUCT, ExceptionType.UPDATE);
        }
    }

    // 게시글 삭제, 성공하면 1페이지로 리다이렉트
    @Transactional
    public void deleteBoard(String boardUuid, String uuid) {
        Board deleteBoard = boardRepository.findByBoardUuidAndBoardStatusTrue(boardUuid)
                .orElseThrow(()->new BoardException(ExceptionType.DELETE));
        // 작성자 본인 확인
        if(deleteBoard.getMemberUuid().equals(uuid)){
            // 게시글 삭제
            deleteBoard.deactiveStatus();
        } else {
            throw new BoardException(ExceptionType.DELETE);
        }
        // 상품이 포함된 게시글의 경우만 비활성화 요청
        switch (deleteBoard.getBoardType()) {
            case SALE, AUCTION -> {
                productService.checkDeleteProduct(boardUuid, uuid);
//  TODO : 이미지를 Board가 들고 있기 vs productService와의 Feign 통신으로 URL받아오기
//                imageService.deleteImage();
            }
        }
    }
}