package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateProductBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationDetailDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationListDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetBoardDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetProductDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.FeignDomainType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.BoardException;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.feignClient.ProductFeignClient;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.util.Auction1PageCache;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final ProductFeignClient productFeignClient;
    private final Auction1PageCache auction1PageCache;
    private final FeignConverter<ProductVo> productConverter;
//    private final FeignConverter<AuctionVo> auctionConverter;

    // 경매 상품 등록
    @Transactional
    public void createActionBoard(RequestCreateProductBoardDto boardDto,
                                       String uuid, String role){
        // 게시글을 작성하기 유효한 ROLE인지 확인
            // 게스트면 불가능
        if(RoleEnums.GUEST.equals(RoleEnums.roleCheck(role))){
            throw new RoleTypeException();
        }
        // 게시글 Entity 생성 - 저장
        Board board = boardDto.toBoard(uuid);
        boardRepository.save(board);

        ProductVo productVo = boardDto.toProduct(board.getBoardUuID());
        // 상품 저장 Feign 요청 및 예외처리
            // ** 이후에 보상 트랜잭션 추가 구현 필요 ( 상품저장이 성공했는데 게시글 저장 실패 )
        productConverter.convertSingleVo(
                productFeignClient.createProductData(productVo, uuid),
                FeignDomainType.PRODUCT,
                ExceptionType.SAVE
        );
    }

    // 판매자 등록 전체 상품 조회
    public ResponseGetCombinationListDto getWriterBoardList(int pageNo, String uuid){
        if(pageNo < 1) pageNo = 0;
        ResponseGetCombinationListDto combinationListDto = new ResponseGetCombinationListDto();
        // 게시글 리스트 담기
        combinationListDto.setBoardList(
                boardRepository
                    .findByMemberUuidAndBoardTypeAndBoardStatusTrue(uuid, BoardType.AUCTION,
                                PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createAt")))
                    .map(ResponseGetBoardDto::new)
        );
        // 상품 리스트 담기
        combinationListDto.setProductList(
                productConverter.convertListVo(productFeignClient.getProductListSeller(uuid, pageNo),
                                                FeignDomainType.PRODUCT,
                                                ExceptionType.GETLIST)
                    .stream()
                    .map(ResponseGetProductDto::new)
                    .toList()
        );
        // 경매 리스트 담기
//        combinationListDto.setAuctionList(
//                auctionConverter.convertListVo(auctionFeignClient.~~)
//        );
        return combinationListDto;
    }

    // 전체 경매 상품 리스트 조회
    public ResponseGetCombinationListDto getAuctionBoardList(int pageNo){
        // 페이지 번호가 -1이나 0인 경우 (1페이지인 경우)
        ResponseGetCombinationListDto combinationListDto = new ResponseGetCombinationListDto();
        combinationListDto.setBoardList(
            (pageNo < 1)
                ?
                //1페이지 캐싱
                auction1PageCache.getPage1()
                :
                // 생성일을 기준으로 내림치순 (최신 글이 먼저 조회)
                boardRepository.findByBoardTypeAndBoardStatusTrue(BoardType.AUCTION,
                        PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createAt")))
                        .map(ResponseGetBoardDto::new)
        );
        // 상품 리스트 담기
        combinationListDto.setProductList(
                productConverter.convertListVo(productFeignClient.getProductList(pageNo)
                        , FeignDomainType.PRODUCT, ExceptionType.GETLIST
                ).stream().map(ResponseGetProductDto::new).toList()
        );
        productConverter.convertListVo(productFeignClient.getProductList(pageNo)
                , FeignDomainType.PRODUCT, ExceptionType.GETLIST
        ).forEach(System.out::println);
        // 경매 리스트 담기
//        combinationListDto.setAuctionList(
//                auctionConverter.convertListVo(auctionFeignClient.~~)
//        );
        return  combinationListDto;
    }

    // 개별 경매 상품 상세 조회
    public ResponseGetCombinationDetailDto getAuctionBoard(String boardUuid){
        ResponseGetCombinationDetailDto combinationDetailDto = new ResponseGetCombinationDetailDto();

        // 해당하는 게시글 가져오기
        Board board = boardRepository.findByBoardUuIDAndBoardStatusTrue(boardUuid)
                .orElseThrow(
                    () ->  new BoardException(ExceptionType.GET)
        );
        combinationDetailDto.setBoardDetail(new ResponseGetBoardDetailDto(board));

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

    // 경매 게시글 업데이트
    @Transactional
    public void updateAuctionBoard(RequestUpdateProductBoardDto updateDTO, String boardUuid, String uuid){
        // 경매 중인지 확인 + 경매가 비활성화 상태면 값 수정
//        getAuctionStatus(boardUuid).updateBoardInfo(updateDTO);
        productConverter.convertSingleVo(
            productFeignClient.updateProductData(boardUuid, updateDTO.toProduct(boardUuid), uuid),
                FeignDomainType.PRODUCT, ExceptionType.UPDATE
        );
    }

    // 경매 게시글 삭제, 성공하면 1페이지로 리다이렉트
    @Transactional
    public void deleteAuctionBoard(String boardUuid, String uuid){
        // 경매 중인지 확인 + 경매가 비활성화 상태면 삭제
//        getAuctionStatus(boardUuid).deactiveStatus();
        // ** 상품도 비활성화되도록 Feign 통신
        productConverter.convertSingleVo(
                productFeignClient.deactiveProductStatus(boardUuid, uuid),
                FeignDomainType.PRODUCT, ExceptionType.UPDATE
        );
    }

    // boardUuid를 통해 Feign통신으로 경매가 비활성화 상태인지 확인
    // product-service가 할 일
//    public Board getAuctionStatus(String boardUuid){
//        // ** Feign 통신 구현 뒤에 수정, auctionCheck가 false (경매 비활성화)의 경우만 삭제 가능
//        // boolean auctionCheck = auctionFeignClient.getAuctionStatusByBoardUuid(boardUuid);
//        boolean auctionCheck = false;
//            // 경매가 활성화 상태면 "경매가 진행 중인 상품이기에 반환 불가능"
//        if(auctionCheck) throw new FeignException(ExceptionType.CHECK);
//        return boardRepository.findByBoardUuIDAndBoardStatus(boardUuid, true)
//                .orElseThrow(() -> new BoardException(ExceptionType.CHECK));
//    }
}
