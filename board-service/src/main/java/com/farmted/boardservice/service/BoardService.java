package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationDetailDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationListDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetProductDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.FeignDomainType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.BoardException;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.feignClient.ProductFeignClient;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.service.subService.NoticeService;
import com.farmted.boardservice.service.subService.ProductService;
import com.farmted.boardservice.util.Board1PageCache;
import com.farmted.boardservice.util.feignConverter.FeignConverter;
import com.farmted.boardservice.vo.ProductVo;
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
    // 1페이징 캐시
    private final Board1PageCache board1PageCache;

    // Feign 통신
    private final ProductFeignClient productFeignClient;
//    private final AuctionFeignClient auctionFeignClient;

    // 서브 서비스
    private final NoticeService noticeService;
    private final ProductService productService;

    // Feign 통신 반환값 컨버터
    private final FeignConverter<ProductVo> productConverter;
//    private final FeignConverter<AuctionVo> auctionConverter;

// 게시글 카테고리별 등록
    @Transactional
    public void createBoard(RequestCreateBoardDto boardDto,
                            String uuid, String role) {
    // 게시글을 작성하기 유효한 ROLE인지 확인
        RoleEnums roleEnums = RoleEnums.roleCheck(role);
        // 게스트면 불가능
        if (RoleEnums.GUEST.equals(roleEnums)) {
            throw new RoleTypeException(roleEnums, boardDto.boardType());
        }

    // 게시글 Entity 생성 - 저장
        Board board = boardDto.toBoard(uuid);
        boardRepository.save(board);
    // 게시글 타입에 따른 하위 도메인 서비스 세팅
        switch(boardDto.boardType()){
            // 상품 서비스에 요청이 필요한 경우 : Feign 요청 및 예외처리
            case SALE, AUCTION -> productService.postProduct(boardDto.toProduct(board.getBoardUuid()), uuid);
            // 일반 게시글은 추가 처리 필요없음.
            case CUSTOMER_SERVICE, COMMISSION -> {return;}
            // 공지사항 : 권한 체크 및 예외처리
            case NOTICE -> noticeService.isAdmin(roleEnums);
            // 상품 : 조회용이기 때문에 게시글이 생성되선 안됨.
            case PRODUCT -> throw new BoardException(boardDto.boardType(), ExceptionType.SAVE);
        }

    // 이후에 보상 트랜잭션 추가 구현 필요 ( 상품저장이 성공했는데 게시글 저장 실패 )
    }

// 전체 게시글 카테고리별 리스트 조회
    public ResponseGetCombinationListDto getAuctionBoardList(BoardType category, int pageNo) {
        ResponseGetCombinationListDto combinationListDto = new ResponseGetCombinationListDto();
    // 게시글 리스트 담기
        combinationListDto.setBoardList(
                // 1페이지 캐싱처리 : 1페이지 && 상품 카테고리인 경우만
                (pageNo < 1 && category.equals(BoardType.PRODUCT))
                    //1페이지 캐싱
                    ? board1PageCache.getPage1()
                    // 생성일을 기준으로 내림치순 (최신 글이 먼저 조회)
                    : boardRepository.findByBoardType(category, PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createdAt")))
        );
    // 상품 리스트 담기
        switch (category) {
            case PRODUCT, SALE, AUCTION -> combinationListDto.setProductList(
                    productConverter.convertListVo(productFeignClient.getProductList(category, pageNo)
                            , FeignDomainType.PRODUCT, ExceptionType.GETLIST
                    ).stream().map(ResponseGetProductDto::new).toList()
            );
        }

    // 경매 리스트 담기 - 경매 전용 카테고리일 따로 조회
        if(BoardType.AUCTION.equals(category))
            combinationListDto.setAuctionList(null);
                        //auctionConverter.convertListVo();
        return combinationListDto;
    }

// 작성자 글 카테고리별 리스트 조회
    public ResponseGetCombinationListDto getWriterBoardList(BoardType category, int pageNo, String uuid, BoardType boardType) {
        if (pageNo < 1) pageNo = 0;
        ResponseGetCombinationListDto combinationListDto = new ResponseGetCombinationListDto();
        // 게시글 리스트 담기
        combinationListDto.setBoardList(
                boardRepository
                        .findByMemberUuidAndBoardType(uuid, category,
                                PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createdAt")))
        );
    //  상품 리스트 담기
        switch (category) {
            case PRODUCT, SALE, AUCTION -> combinationListDto.setProductList(
                    productConverter.convertListVo(productFeignClient.getProductListSeller(uuid, category, pageNo)
                            , FeignDomainType.PRODUCT, ExceptionType.GETLIST
                    ).stream().map(ResponseGetProductDto::new).toList()
            );
        }
    // 경매 전용 카테고리일 따로 조회
        if(BoardType.AUCTION.equals(category))
            combinationListDto.setAuctionList(null);
        return combinationListDto;
    }



    // 개별 경매 상품 상세 조회
    public ResponseGetCombinationDetailDto getAuctionBoard(String boardUuid) {
        ResponseGetCombinationDetailDto combinationDetailDto = new ResponseGetCombinationDetailDto();
        // 해당하는 게시글 가져오기
        combinationDetailDto.setBoardDetail(boardRepository.findDetailByBoardUuid(boardUuid)
                .orElseThrow(() -> new BoardException(ExceptionType.GET)));

        // 해당하는 상품 가져오기
        combinationDetailDto.setProductDetail(
                new ResponseGetProductDetailDto(
                        productConverter.convertSingleVo(
                                productFeignClient.getProductDetail(boardUuid),
                                FeignDomainType.PRODUCT, ExceptionType.GET)
                )
        );
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

    // 상품 게시글 업데이트
    @Transactional
    public void updateAuctionBoard(RequestUpdateProductBoardDto updateDTO, String boardUuid, String uuid) {
        // 게시글 수정
        boardRepository.findByBoardUuid(boardUuid)
            .orElseThrow(()->new BoardException(ExceptionType.UPDATE)).updateBoardInfo(updateDTO);
        // 상품에 수정 요청
        productConverter.convertSingleVo(
                productFeignClient.updateProductData(boardUuid, updateDTO.toProduct(boardUuid), uuid),
                FeignDomainType.PRODUCT, ExceptionType.UPDATE
        );
    }

    // 경매 게시글 삭제, 성공하면 1페이지로 리다이렉트
    @Transactional
    public void deleteAuctionBoard(String boardUuid, String uuid) {
        // 게시글 삭제
        boardRepository.findByBoardUuid(boardUuid)
                .orElseThrow(()->new BoardException(ExceptionType.UPDATE)).deactiveStatus();
        // ** 상품도 비활성화되도록 Feign 통신
        productConverter.convertSingleVo(
                productFeignClient.deactiveProductStatus(boardUuid, uuid),
                FeignDomainType.PRODUCT, ExceptionType.UPDATE
        );
    }
}