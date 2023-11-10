package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetAuctionBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.vo.AuctionVo;
import com.farmted.boardservice.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 경매 상품 등록
    @Transactional
    public ProductVo createActionBoard(RequestCreateProductBoardDto boardDto,
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
    public List<ResponseGetAuctionBoardDto> getAuctionBoardList(){
        // 비회원도 확인할 수 있는 메서드이므로 role 확인하지 않음
        List<ResponseGetAuctionBoardDto> responseBoardList = new ArrayList<>();
        // 타입이 경매인 전체 게시글 조회
            // 나중엔 Board에서 필요한 값만 꺼내 DTO로 활용할 예정
        List<Board> boardList = boardRepository.findAllByBoardTypeAndBoardStatus(BoardType.AUCTION, true);

        for(Board board : boardList){
            ResponseGetAuctionBoardDto responseBoard = new ResponseGetAuctionBoardDto();

            // *** 게시글 UUID 받아와서 경매와 상품에 Feign 요청
                // feign 로직 생성 뒤에 exception 처리 예정
            String uuid = board.getBoardUuID();
            ProductVo productVo = ProductVo.createDummyProduct(uuid);
            AuctionVo auctionVo = AuctionVo.createDummyAuction(uuid);

            // responseDto에 데이터 담기
            responseBoard.getBoard(board);
            responseBoard.getProduct(productVo);
            responseBoard.getAuction(auctionVo);

            // list에 dto 담기
            responseBoardList.add(responseBoard);
        }

        return  responseBoardList;
    }

    // 개별 경매 상품 상세 조회
    public ResponseGetAuctionBoardDto getAuctionBoard(String boardUuid){
        // 해당하는 게시글 가져오기
        Board board = boardRepository.findByBoardUuIDAndBoardStatus(boardUuid, true).orElseThrow(
                () ->  new RuntimeException("해당하는 게시글이 없습니다.")
        );
        ResponseGetAuctionBoardDto responseBoard = new ResponseGetAuctionBoardDto();
        // 필요한 값을 레포/통신을 통해 받아 dto에 담기
        responseBoard.getBoard(board);
            // ** Feign통신
        responseBoard.getProduct(ProductVo.createDummyProduct(boardUuid));
        responseBoard.getAuction(AuctionVo.createDummyAuction(boardUuid));

        return responseBoard;
    }

}
