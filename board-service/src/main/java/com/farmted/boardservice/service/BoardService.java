package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationDetailDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationListDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.exception.BoardException;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.util.Board1PageCache;
import com.farmted.boardservice.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {
    // 레포지토리
    private final BoardRepository boardRepository;
    // 1페이징 캐시 : 카테고리가 PRODUCT(SALE+AUCTION)인 경우의 1페이지
    private final Board1PageCache board1PageCache;

// 게시글 카테고리별 등록
    public String createBoard(RequestCreateBoardDto boardDto,
                            String uuid, MemberVo memberInfo) {
    // 게시글 Entity 생성 - 저장
        Board board = boardDto.toBoard(uuid, memberInfo);
        boardRepository.save(board);
        return board.getBoardUuid();
    }

// 전체 게시글 카테고리별 리스트 조회
    public ResponseGetCombinationListDto getBoardList(BoardType category, int pageNo) {
        ResponseGetCombinationListDto combinationListDto = new ResponseGetCombinationListDto();
        // 게시글 리스트 담기
        combinationListDto.setPageList(
                // 1페이지 캐싱처리 : 1페이지 && 상품 카테고리인 경우만
                (pageNo < 1 && category.equals(BoardType.PRODUCT))
                    //1페이지 캐싱
                    ? board1PageCache.getPage1()
                    // 생성일을 기준으로 내림치순 (최신 글이 먼저 조회)
                    : boardRepository.findByBoardType(category,
                        PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createdAt")))
        , pageNo);
        return combinationListDto;
    }

// 작성자 글 카테고리별 리스트 조회 (판매자 입장)
    public ResponseGetCombinationListDto getWriterBoardList(BoardType category, int pageNo, String sellerUuid) {
        ResponseGetCombinationListDto combinationListDto = new ResponseGetCombinationListDto();
        // 게시글 리스트 담기
        combinationListDto.setPageList(
                boardRepository
                        .findByMemberUuidAndBoardType(sellerUuid, category,
                                PageRequest.of(pageNo, 3, Sort.by(Sort.Direction.DESC, "createdAt")))
                ,pageNo
        );
        return combinationListDto;
    }



    // 개별 경매 상품 상세 조회
    public ResponseGetCombinationDetailDto getBoard(String boardUuid) {
        ResponseGetCombinationDetailDto combinationDetailDto = new ResponseGetCombinationDetailDto();
        // 해당하는 게시글 가져오기
        combinationDetailDto.setBoardDetail(boardRepository.findDetailByBoardUuid(boardUuid)
                .orElseThrow(() -> new BoardException(ExceptionType.GET)));
        return combinationDetailDto;
    }

    // 게시글 업데이트
    public boolean updateBoard(RequestUpdateProductBoardDto updateDTO, String boardUuid, String uuid) {
        Board updateBoard = boardRepository.findByBoardUuidAndBoardStatusTrue(boardUuid)
                .orElseThrow(()->new BoardException(ExceptionType.UPDATE));
        // 카테고리 변경의 경우, 판매 <-> 경매(종료된)만 가능
        // 상품이 포함된 게시글의 경우만 수정 요청
        switch (updateBoard.getBoardType()){
            case SALE, AUCTION -> {
                // Product 값을 변경할 수 있는 경우만 true
                if (Objects.requireNonNull(updateDTO.boardType()) == BoardType.SALE || updateDTO.boardType() == BoardType.AUCTION) {
                    updateBoard.updateBoardInfo(updateDTO);
                    return true;
                }
            }
            // 기존 게시글의 경우 타입 변경 불가능
            case NOTICE, COMMISSION, CUSTOMER_SERVICE -> {
                if (updateBoard.getBoardType().equals(updateDTO.boardType())) {
                    updateBoard.updateBoardInfo(updateDTO);
                    return true;
                }
            }
        }
        // 업데이트 안됐으면 다 예외
        throw new BoardException(updateBoard.getBoardType(), ExceptionType.UPDATE);
    }

    // 게시글 삭제, 성공하면 1페이지로 리다이렉트
    public BoardType deleteBoard(String boardUuid, String uuid) {
        Board deleteBoard = boardRepository.findByBoardUuidAndBoardStatusTrue(boardUuid)
                .orElseThrow(()->new BoardException(ExceptionType.DELETE));
        // 작성자 본인 확인
        if(deleteBoard.getMemberUuid().equals(uuid)){
            // 게시글 삭제
            deleteBoard.deactiveStatus();
        } else {
            throw new BoardException(ExceptionType.DELETE);
        }
        return deleteBoard.getBoardType();
    }
}