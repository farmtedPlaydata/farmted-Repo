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
import com.farmted.boardservice.service.subService.NoticeService;
import com.farmted.boardservice.service.subService.ProductService;
import com.farmted.boardservice.util.Board1PageCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
// Board-Service 전체의 로직을 담당
// 1. Board-Repository 관리 : 트랜잭션 처리
// 2. responseDto 반환을 수행 : 컨트롤러에 반환
public class BoardService {
    // 레포지토리
    private final BoardRepository boardRepository;
    // 1페이징 캐시 (카테고리가 PRODUCT(SALE+AUCTION)인 경우의 1페이지
    private final Board1PageCache board1PageCache;
    // 서브 서비스 (Feign 통신의 결과, 예외처리 담당)
    private final NoticeService noticeService;
    private final ProductService productService;
    private final AuctionService auctionService;
    private final MemberService memberService;

// 게시글 카테고리별 등록
    @Transactional
    public void createBoard(RequestCreateBoardDto boardDto,
                            String uuid, RoleEnums role) {
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
            // 상품 서비스에 요청이 필요한 경우 : Feign 요청 및 예외처리
            case SALE, AUCTION -> productService.postProduct(boardDto.toProduct(board.getBoardUuid()), uuid);
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

// 구매자 입장 게시글 카테고리별 리스트 조회
    // 이 경우엔 역으로 Auction에서 받아온 BoardUuid를 기반으로 Board가 Uuid를 만들어야 함
    // 그럼 SALE의 경우는 어떻게 체크 해야하지? (Order만들어야하나?)
//    public ResponseGetCombinationListDto getBuyerBoardList(BoardType category, int pageNo, String buyerUuid){
//        ResponseGetCombinationListDto combinationListDto = new ResponseGetCombinationListDto();
//        switch (category){
//            // 당장 경매의 경우만 구현함
//            case AUCTION -> combinationListDto.setAuctionList(auctionService.getSellerAuctionList(buyerUuid));
//            case SALE -> {}//어캄?
//            case PRODUCT -> {}
//        }
//        // 이럼 경매에서 Page 정보도 반환받아야 함
//            // 차라리 낙찰 리스트의 경우는 상품명+낙찰가+판매자명+boardUuid만 표기하는 페이징 리스트를
//            // Board-Service를 통하지 않고 Auction이 직접 반환하는게 나을듯
//        List<ResponseGetBoardDto> combiBoardDto = new ArrayList<>();
//        for(ResponseGetAuctionDto auction : combinationListDto.getAuctionList()){
//            combiBoardDto.add(boardRepository.findBidderByBoardUuid(auction.getBoardUuid())
//                    .orElseThrow(()->new BoardException(ExceptionType.GETLIST)));
//        }
//        Page<ResponseGetBoardDto> pageBoardDto =
//        combinationListDto.setBoardList(combiBoardDto);
//
//        return combinationListDto;
//    }

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
                    auctionService.getBuyerAuctionList(sellerUuid)
            );
        return combinationListDto;
    }



    // 개별 경매 상품 상세 조회
    public ResponseGetCombinationDetailDto getBoard(String boardUuid) {
        ResponseGetCombinationDetailDto combinationDetailDto = new ResponseGetCombinationDetailDto();
        // 해당하는 게시글 가져오기
        combinationDetailDto.setBoardDetail(boardRepository.findDetailByBoardUuid(boardUuid)
                .orElseThrow(() -> new BoardException(ExceptionType.GET)));

        // 해당하는 상품 가져오기
        combinationDetailDto.setProductDetail(productService.getProductByBoardUuid(boardUuid));
        // 해당하는 경매 가져오기
//        combinationDetailDto.setAuctionDetail(
//                new ResponseGetAuctionDetailDto(
//                        auctionConverter.convertSingleVo(
//                                ,
//                                FeignDomainType.AUCTION, ExceptionType.GET
//                        )
//                )
//        );
        return combinationDetailDto;
    }

    // 게시글 업데이트
    @Transactional
    public void updateBoard(RequestUpdateProductBoardDto updateDTO, String boardUuid, String uuid) {
        Board updateBoard = boardRepository.findByBoardUuidAndBoardStatusTrue(boardUuid)
                .orElseThrow(()->new BoardException(ExceptionType.UPDATE));
        // 게시글 수정
        updateBoard.updateBoardInfo(updateDTO);
        // 상품이 포함된 게시글의 경우만 수정 요청
        switch (updateBoard.getBoardType()){
            case PRODUCT, SALE, AUCTION -> productService.checkUpdateProduct(boardUuid, updateDTO, uuid);
        }
    }

    // 게시글 삭제, 성공하면 1페이지로 리다이렉트
    @Transactional
    public void deleteBoard(String boardUuid, String uuid) {
        Board deleteBoard = boardRepository.findByBoardUuidAndBoardStatusTrue(boardUuid)
                .orElseThrow(()->new BoardException(ExceptionType.DELETE));
        // 게시글 삭제
        deleteBoard.deactiveStatus();
        // 상품이 포함된 게시글의 경우만 비활성화 요청
        switch (deleteBoard.getBoardType()) {
            case PRODUCT, SALE, AUCTION -> productService.checkDeleteProduct(boardUuid, uuid);
        }
    }
}