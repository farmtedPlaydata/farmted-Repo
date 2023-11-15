package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateProductBoardDTO;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDTO;
import com.farmted.boardservice.dto.response.ResponseGetAuctionBoardDTO;
import com.farmted.boardservice.dto.response.ResponseGetAuctionBoardListDTO;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.exception.UpdateBoardException;
import com.farmted.boardservice.feignClient.AuctionFeignClient;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.vo.AuctionVo;
import com.farmted.boardservice.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
//    private final AuctionFeignClient auctionFeignClient;

    // 경매 상품 등록
    @Transactional
    public ProductVo createActionBoard(RequestCreateProductBoardDTO boardDto,
                                       String uuid, String role){
        // 게시글을 작성하기 유효한 ROLE인지 확인
            // 게스트면 불가능
        if(RoleEnums.GUEST.equals(RoleEnums.roleCheck(role))){
            throw new RoleTypeException();
        }
        // 게시글 Entity 생성 - 저장
        Board board = boardDto.toBoard(uuid);
        boardRepository.save(board);

        // *** 상품 VO 생성 (+ 생성된 게시글UUID를 추가로 담기) - 전송
        return boardDto.toProduct(board.getBoardUuID());
    }

    // 전체 경매 상품 리스트 조회
    public List<ResponseGetAuctionBoardListDTO> getAuctionBoardList(){
        // 비회원도 확인할 수 있는 메서드이므로 role 확인하지 않음
        List<ResponseGetAuctionBoardListDTO> responseBoardList = new ArrayList<>();
        // 타입이 경매인 전체 게시글 조회
            // 나중엔 Board에서 필요한 값만 꺼내 DTO로 활용할 예정
        List<Board> boardList = boardRepository.findAllByBoardTypeAndBoardStatus(BoardType.AUCTION, true);

        for(Board board : boardList){
            // list에 dto 담기
            responseBoardList.add(new ResponseGetAuctionBoardListDTO(board));

            // *** 게시글 UUID 받아와서 경매와 상품에 Feign 요청
                // feign 로직 생성 뒤에 exception 처리 예정
//            String uuid = board.getBoardUuID();
//            ProductVo productVo = ProductVo.createDummyProduct(uuid);
//            AuctionVo auctionVo = AuctionVo.createDummyAuction(uuid);

//            // responseDto에 데이터 담기
//            responseBoard.getBoard(board);
//            responseBoard.getProduct(productVo);
//            responseBoard.getAuction(auctionVo);
        }

        return  responseBoardList;
    }

    // 개별 경매 상품 상세 조회
    public ResponseGetAuctionBoardDTO getAuctionBoard(String boardUuid){
        // 해당하는 게시글 가져오기
        Board board = boardRepository.findByBoardUuIDAndBoardStatus(boardUuid, true).orElseThrow(
                () ->  new RuntimeException("해당하는 게시글이 없습니다.")
        );
        ResponseGetAuctionBoardDTO responseBoard = new ResponseGetAuctionBoardDTO();
        // 필요한 값을 레포/통신을 통해 받아 dto에 담기
        responseBoard.assignBoard(board);
//            // ** Feign통신
//        responseBoard.getProduct(ProductVo.createDummyProduct(boardUuid));
//        responseBoard.getAuction(AuctionVo.createDummyAuction(boardUuid));

        return responseBoard;
    }

    // 경매 게시글 삭제, 성공하면 1페이지로 리다이렉트
    @Transactional
    public void deleteAuctionBoard(String boardUuid){
        // 경매 중인지 확인 + 경매가 비활성화 상태면 삭제
        getAuctionStatus(boardUuid).deactiveStatus();
        // ** 상품도 비활성화되도록 Feign 통신
    }

    // 경매 게시글 업데이트
    @Transactional
    public ProductVo updateAuctionBoard(RequestUpdateProductBoardDTO updateDTO, String boardUuid){
        // 경매 중인지 확인 + 경매가 비활성화 상태면 값 수정
        getAuctionStatus(boardUuid).updateBoardInfo(updateDTO);
        // ** 상품 값도 변경되도록 Feign 통신
        return updateDTO.toProduct(boardUuid);
    }

    // boardUuid를 통해 Feign통신으로 경매가 비활성화 상태인지 확인
    public Board getAuctionStatus(String boardUuid){
        // ** Feign 통신 구현 뒤에 수정, auctionCheck가 false (경매 비활성화)의 경우만 삭제 가능
        // boolean auctionCheck = auctionFeignClient.getAuctionStatusByBoardUuid(boardUuid);
        boolean auctionCheck = false;
            // 경매가 활성화 상태면 "경매가 진행 중인 상품이기에 반환 불가능"
        if(auctionCheck) throw new UpdateBoardException();
        return boardRepository.findByBoardUuIDAndBoardStatus(boardUuid, true)
                .orElseThrow(() -> new UpdateBoardException(boardUuid));
    }
}
