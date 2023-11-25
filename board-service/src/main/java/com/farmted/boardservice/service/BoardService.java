package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateProductBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationListDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetBoardDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.FeignDomainType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.BoardException;
import com.farmted.boardservice.exception.FeignException;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.feignClient.ProductFeignClient;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.util.Auction1PageCache;
import com.farmted.boardservice.util.CatchingFeign;
import com.farmted.boardservice.vo.AuctionVo;
import com.farmted.boardservice.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ProductFeignClient productFeignClient;
    private final Auction1PageCache auction1PageCache;
    private final CatchingFeign catchingFeign;

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

        // 상품 저장 Feign 요청 및 예외처리
            // ** 이후에 보상 트랜잭션 추가 구현 필요 ( 상품저장이 성공했는데 게시글 저장 실패 )
        catchingFeign.findFeignData(
                productFeignClient
                        .createProductData(boardDto.toProduct(board.getBoardUuID()), uuid),
                        FeignDomainType.PRODUCT,
                        ExceptionType.SAVE);
    }

    // 판매자 등록 전체 상품 조회
    public ResponseGetCombinationListDto getSellerBoardList(int pageNo, String uuid){
        ResponseGetCombinationListDto combinationListDto = null;
        // 게시글 리스트 담기
        combinationListDto.setBoardList(
                boardRepository
                    .findByMemberUuidBoardTypeAndBoardStatus(uuid, BoardType.AUCTION, true,
                        PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createAt")))
                .map(ResponseGetBoardDto::new)
        );
        // 상품 리스트 담기
        combinationListDto.setProductList(
                ((List<ProductVo>)catchingFeign
                    .findFeignData(productFeignClient.getProductListSeller(uuid, pageNo),
                        FeignDomainType.PRODUCT,
                        ExceptionType.GETLIST)
                    .getData())
                .stream()
                .map(ResponseGetProductDto::new)
                .collect(Collectors.toList())
        );
        return combinationListDto;
    }

    // 전체 경매 상품 리스트 조회
    public Page<ResponseGetBoardDto> getAuctionBoardList(int pageNo){
        // 페이지 번호가 -1이나 0인 경우 (1페이지인 경우)
        Page<ResponseGetBoardDto> responseBoardList = null;
        if(pageNo < 1){
            //1페이지 캐싱
            responseBoardList = auction1PageCache.getPage1();
        }else{
            // 생성일을 기준으로 내림치순 (최신 글이 먼저 조회)
            responseBoardList = boardRepository.findByBoardTypeAndBoardStatus(BoardType.AUCTION, true,
                    PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createAt")))
                    .map(ResponseGetBoardDto::new);
        }

        // 확실히 Board list에 대한 개별 상품/경매 정보를 받아오면 통신 비용이 너무 발생할거 같다.
            // 상품과 경매에서 slice로 받아온 데이터를 붙이는 방식으로 해야할 듯
            // 데이터 정합성이 깨질 수 있는 가능성에 대해서는 방안을 생각해보는 방식으로
        // *** 판매자 이름 Slice 통신 요청
        // *** 상품 정보 Slice 통신 요청
        // *** 경매 정보 Slice 통신 요청
        for(ResponseGetBoardDto responseBoard : responseBoardList.getContent()){
            // *** 게시글 UUID 받아와서 경매와 상품에 Feign 요청
                // feign 로직 생성 뒤에 exception 처리 예정
            String boardUuid = responseBoard.getBoardUuid();
            ProductVo productVo = ProductVo.createDummyProduct(boardUuid);
            AuctionVo auctionVo = AuctionVo.createDummyAuction(boardUuid);
            // responseDto에 데이터 담기
            responseBoard.addProduct(productVo);
            responseBoard.addAuction(auctionVo);
        }

        return  responseBoardList;
    }

    // 개별 경매 상품 상세 조회
    public ResponseGetBoardDetailDto getAuctionBoard(String boardUuid){
        // 해당하는 게시글 가져오기
        Board board = boardRepository.findByBoardUuIDAndBoardStatus(boardUuid, true).orElseThrow(
                () ->  new BoardException(ExceptionType.GET)
        );
        ResponseGetBoardDetailDto responseBoard = new ResponseGetBoardDetailDto();
        // 필요한 값을 레포/통신을 통해 받아 dto에 담기
        responseBoard.assignBoard(board);

        // ** Feign통신 내부에 값이 하나라도 있으면 통신 성공한 것
//        responseBoard.addProduct(Optional.ofNullable(productFeignClient.getProductData(boardUuid))
//                    .filter(ProductVo::isFilled)
//                        .orElseThrow(() -> new ProductFeignException(ExceptionType.GET)));

        responseBoard.addAuction(AuctionVo.createDummyAuction(boardUuid));

        return responseBoard;
    }

    // 경매 게시글 업데이트
    @Transactional
    public void updateAuctionBoard(RequestUpdateProductBoardDto updateDTO, String boardUuid, String uuid){
        // 경매 중인지 확인 + 경매가 비활성화 상태면 값 수정
        getAuctionStatus(boardUuid).updateBoardInfo(updateDTO);

        // ** 상품 값도 변경되도록 Feign 통신
        // Feign 통신 실패시 (false 반환 시)
//        if(!productFeignClient.updateProductData(boardUuid, updateDTO.toProduct(boardUuid),uuid))
//            throw new ProductFeignException(ExceptionType.UPDATE);
    }

    // 경매 게시글 삭제, 성공하면 1페이지로 리다이렉트
    @Transactional
    public void deleteAuctionBoard(String boardUuid, String uuid){
        // 경매 중인지 확인 + 경매가 비활성화 상태면 삭제
        getAuctionStatus(boardUuid).deactiveStatus();
        // ** 상품도 비활성화되도록 Feign 통신
        productFeignClient.deactiveProductStatus(boardUuid, uuid);
    }

    // boardUuid를 통해 Feign통신으로 경매가 비활성화 상태인지 확인
    public Board getAuctionStatus(String boardUuid){
        // ** Feign 통신 구현 뒤에 수정, auctionCheck가 false (경매 비활성화)의 경우만 삭제 가능
        // boolean auctionCheck = auctionFeignClient.getAuctionStatusByBoardUuid(boardUuid);
        boolean auctionCheck = false;
            // 경매가 활성화 상태면 "경매가 진행 중인 상품이기에 반환 불가능"
        if(auctionCheck) throw new FeignException(ExceptionType.CHECK);
        return boardRepository.findByBoardUuIDAndBoardStatus(boardUuid, true)
                .orElseThrow(() -> new BoardException(ExceptionType.CHECK));
    }
}
