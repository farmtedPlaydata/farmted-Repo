package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateProductBoardDTO;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDTO;
import com.farmted.boardservice.dto.response.ResponseGetAuctionBoardDTO;
import com.farmted.boardservice.dto.response.ResponseGetAuctionBoardListDTO;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.vo.ProductVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ServiceTest {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @Autowired
    public ServiceTest(BoardService boardService, BoardRepository boardRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }

    @BeforeEach
    public void reset(){
        boardRepository.deleteAll();
        // 더미데이터 생성
        RequestCreateProductBoardDTO dummyData = new RequestCreateProductBoardDTO(
                BoardType.AUCTION,             // BoardType 값
                "게시글 내용",                  // 게시글 내용
                "게시글 제목",                  // 게시글 제목
                "상품 이름",                    // 상품 이름
                10,                             // 상품 재고
                10_000L,                         // 상품 가격
                "상품 출처",                    // 상품 출처
                "상품 이미지 URL"               // 상품 이미지 URL
        );
        RequestCreateProductBoardDTO dummyData2 = new RequestCreateProductBoardDTO(
                BoardType.AUCTION,             // BoardType 값
                "게시글 내용2",                  // 게시글 내용
                "게시글 제목2",                  // 게시글 제목
                "상품 이름2",                    // 상품 이름
                20,                             // 상품 재고
                20_000L,                         // 상품 가격
                "상품 출처2",                    // 상품 출처
                "상품 이미지 URL2"               // 상품 이미지 URL
        );
        boardService.createActionBoard(dummyData, "uuid", "ROLE_USER");
        boardService.createActionBoard(dummyData2, "uuid", "ROLE_USER");
    }

// Create 로직
    // 일반적인 생성 로직
    @Test
    public void createBoardTest(){
        // given
            // 더미데이터 생성
        RequestCreateProductBoardDTO dummyData = new RequestCreateProductBoardDTO(
                BoardType.AUCTION,             // BoardType 값
                "게시글 내용",                  // 게시글 내용
                "Example",                  // 게시글 제목
                "상품 이름",                    // 상품 이름
                10,                             // 상품 재고
                10_000L,                         // 상품 가격
                "상품 출처",                    // 상품 출처
                "상품 이미지 URL"               // 상품 이미지 URL
        );

        // when
            // 통신을 배제해서 게시글 데이터는 저장하고
            // 상품 데이터는 컨트롤러로 반환
         ProductVo productVo = boardService.createActionBoard(dummyData, "userUUID", "ROLE_USER");

        // then
        Board checkBoard = boardRepository.findAllByMemberUuidAndBoardStatus("userUUID", true).get(0);
            // 1. 저장한 Board와 더미데이터의 데이터 값이 같은가
        assertThat(dummyData.boardContent())
                .isEqualTo(checkBoard.getBoardContent());
            // 2. 전송할 ProductVO가 입력한 값과 같은가
        assertThat(productVo.toString()).isEqualTo(
                dummyData.toProduct(checkBoard.getBoardUuID()).toString()
                );
    }
    // Create 예외 확인 로직
        // 주어진 Role이 존재하지 않는 값인 경우
    @Test
    public void createBoardWhenWeirdRole(){
        // given
        // 더미데이터 생성
        RequestCreateProductBoardDTO dummyData = new RequestCreateProductBoardDTO(
                BoardType.AUCTION,             // BoardType 값
                "게시글 내용",                  // 게시글 내용
                "Example",                  // 게시글 제목
                "상품 이름",                    // 상품 이름
                10,                             // 상품 재고
                10_000L,                         // 상품 가격
                "상품 출처",                    // 상품 출처
                "상품 이미지 URL"               // 상품 이미지 URL
        );

        // when
        // then
        // 주어진 Role이 유효하지 않은 ROLE값인 경우 예외처리
        Assertions.assertThrows(RoleTypeException.class, ()->boardService.createActionBoard(dummyData, "userUUID", "ROLE_ROLE"));
    }
        // 유저가 게스트인 경우
    @Test
    public void createBoardWhenGuest(){
        // given
        // 더미데이터 생성
        RequestCreateProductBoardDTO dummyData = new RequestCreateProductBoardDTO(
                BoardType.AUCTION,             // BoardType 값
                "게시글 내용",                  // 게시글 내용
                "Example",                  // 게시글 제목
                "상품 이름",                    // 상품 이름
                10,                             // 상품 재고
                10_000L,                         // 상품 가격
                "상품 출처",                    // 상품 출처
                "상품 이미지 URL"               // 상품 이미지 URL
        );

        // when
        // then
            // 주어진 Role이 GUEST인 경우 실패해야함
        Assertions.assertThrows(RoleTypeException.class, ()->boardService.createActionBoard(dummyData, "userUUID", "ROLE_GUEST"));
    }


// Read 로직
    // 전체 경매 게시글 조회
    @Test
    public void getAuctionBoardList(){
        // given
        // when
        List<ResponseGetAuctionBoardListDTO> responseDto = boardService.getAuctionBoardList();

        // then
        assertThat(responseDto.size()).isEqualTo(2);
    }

    // 개별 경매 게시글 조회
    @Test
    public void getAuctionBoard(){
        // given
        // 더미데이터 생성
        Board board = boardRepository.findAll().get(0);

        // when
        ResponseGetAuctionBoardDTO responseDto = boardService.getAuctionBoard(board.getBoardUuID());

        // then
        assertThat(responseDto.getBoardTitle()).isEqualTo(board.getBoardTitle());
    }

// Update + Delete 로직 (소프트 딜리트 예정이기에 사실상 업데이트 로직)
    // DELETE 메소드
        // 경매 게시글 삭제 (비활성화)
    @Test
    public void deleteAuctionBoard(){
        // given
            // 주어진 uuid로 해당 게시글 비활성화
        String boardUuid = boardRepository.findAll().get(0).getBoardUuID();
        // when
        boardService.deleteAuctionBoard(boardUuid);
        // then
            // 해당 uuid를 가진 활성화된 게시글은 없지만, 비활성화된 게시글은 있어야 함
        assertThat(boardRepository.findByBoardUuIDAndBoardStatus(boardUuid, true)).isEqualTo(Optional.empty());
        assertThat(boardRepository.findByBoardUuIDAndBoardStatus(boardUuid, false).get()).isNotNull();
    }
        // 입력받은 게시글UUID가 유효하지 않는 경우
    @Test
    public void deleteAuctionBoardWithWeirdBoardUuid(){
        // given
        String boardUuid = "123";
        // when
        // then
        Assertions.assertThrows(RuntimeException.class, ()-> boardService.deleteAuctionBoard(boardUuid));
    }
    // PUT 메소드
        // 경매 게시글 업데이트
    @Test
    public void updateAuctionBoard(){
        // given
        RequestUpdateProductBoardDTO dummyData = new RequestUpdateProductBoardDTO(
                BoardType.SALE,           // BoardType 값
                "수정된 게시글 내용",         // 수정된 게시글 내용
                "수정된 게시글 제목",         // 수정된 게시글 제목
                "수정된 상품 이름",           // 수정된 상품 이름
                20,                           // 수정된 상품 재고
                15_000L,                      // 수정된 상품 가격
                "수정된 상품 출처",           // 수정된 상품 출처
                "수정된 상품 이미지 URL"      // 수정된 상품 이미지 URL
        );
        String boardUuid = boardRepository.findAll().get(0).getBoardUuID();

        // when
            // 업데이트 로직 실행
        ProductVo productVo = boardService.updateAuctionBoard(dummyData, boardUuid);
        // then
            // 업데이트가 정상적으로 진행되었는지
        assertThat(boardRepository.findByBoardUuIDAndBoardStatus(boardUuid,true).get().getBoardTitle())
                .isEqualTo(dummyData.boardTitle());
            // 반환된 productVo값이 같은지
        assertThat(productVo.toString())
                .isEqualTo(dummyData.toProduct(boardUuid).toString());
    }
}
