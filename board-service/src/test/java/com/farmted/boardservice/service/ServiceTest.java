package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateProductBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetAuctionBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetAuctionBoardListDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.vo.ProductVo;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
    // 스케줄러 cron값 임의 변경 가능하도록
//@TestPropertySource(properties = "schedules.cron= * * * * * *")
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
        IntStream.rangeClosed(1, 5).forEach( (i)->{
                boardService.createActionBoard(new RequestCreateProductBoardDto(
                    BoardType.AUCTION,             // BoardType 값
                    "게시글 내용"+i,                  // 게시글 내용
                    "게시글 제목"+i,                  // 게시글 제목
                    "상품 이름"+i,                    // 상품 이름
                    10*i,                             // 상품 재고
                    10_000L*i,                         // 상품 가격
                    "상품 출처"+i,                    // 상품 출처
                    "상품 이미지 URL"+i               // 상품 이미지 URL
                ), "uuid"+i, "ROLE_USER");
                }
        );
    }

// Create 로직
    // 일반적인 생성 로직
    @Test
    public void createBoardTest(){
        // given
            // 더미데이터 생성
        RequestCreateProductBoardDto dummyData = new RequestCreateProductBoardDto(
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
//         ProductVo productVo = boardService.createActionBoard(dummyData, "userUUID", "ROLE_USER");
//
//        // then
//        Board checkBoard = boardRepository.findAllByMemberUuidAndBoardStatus("userUUID", true).get(0);
//            // 1. 저장한 Board와 더미데이터의 데이터 값이 같은가
//        assertThat(dummyData.boardContent())
//                .isEqualTo(checkBoard.getBoardContent());
//            // 2. 전송할 ProductVO가 입력한 값과 같은가
//        assertThat(productVo.toString()).isEqualTo(
//                dummyData.toProduct(checkBoard.getBoardUuID()).toString()
//                );
    }
    // Create 예외 확인 로직
        // 주어진 Role이 존재하지 않는 값인 경우
    @Test
    public void createBoardWhenWeirdRole(){
        // given
        // 더미데이터 생성
        RequestCreateProductBoardDto dummyData = new RequestCreateProductBoardDto(
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
        RequestCreateProductBoardDto dummyData = new RequestCreateProductBoardDto(
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
        // 1페이지인 경우 ( 페이징 캐시 )
    @Test
    public void getAuctionBoardPage1() throws InterruptedException {
        // given
        int page = 1;

        // when
        // then
            // 1초 단위 스케줄러 실행을 위해 2초 대기
        System.out.println("@@@ + "+ boardRepository.findByBoardTypeAndBoardStatus(BoardType.AUCTION, true,
                        PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createAt")))
                .map(ResponseGetAuctionBoardListDto::new).getContent());
        Awaitility.await().atMost(5000, TimeUnit.MILLISECONDS)
                .until( ()-> {
                            // 여기서 스케줄러에 의해 업데이트된 값을 가져와서 검증
                            Page<ResponseGetAuctionBoardListDto> responseDto = boardService.getAuctionBoardList(page - 1);
                            return responseDto.getContent().size() == 3;
                });
        System.out.println("####"+boardRepository.findAll().size());
    }
        // 1페이지가 아닌 경우
    @Test
    public void getAuctionBoardList(){
        // given
        int page = 2;
        // when
        Page<ResponseGetAuctionBoardListDto> responseDto = boardService.getAuctionBoardList(page-1);

        // then
        assertThat(responseDto.getContent().size()).isEqualTo(2);
    }

    // 개별 경매 게시글 조회
    @Test
    public void getAuctionBoard(){
        // given
        // 더미데이터 생성
        Board board = boardRepository.findAll().get(0);

        // when
        ResponseGetAuctionBoardDto responseDto = boardService.getAuctionBoard(board.getBoardUuID());

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
        String uuid = "uuid1";
        // when
        boardService.deleteAuctionBoard(boardUuid, uuid);
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
        String uuid = "uuid1";
        // when
        // then
        Assertions.assertThrows(RuntimeException.class, ()-> boardService.deleteAuctionBoard(boardUuid, uuid));
    }
    // PUT 메소드
        // 경매 게시글 업데이트
    @Test
    public void updateAuctionBoard(){
        // given
        RequestUpdateProductBoardDto dummyData = new RequestUpdateProductBoardDto(
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
        String uuid = "uuid1";

        // when
            // 업데이트 로직 실행
        boardService.updateAuctionBoard(dummyData, boardUuid, uuid);
        // then
            // 업데이트가 정상적으로 진행되었는지
        assertThat(boardRepository.findByBoardUuIDAndBoardStatus(boardUuid,true).get().getBoardTitle())
                .isEqualTo(dummyData.boardTitle());
    }
}
