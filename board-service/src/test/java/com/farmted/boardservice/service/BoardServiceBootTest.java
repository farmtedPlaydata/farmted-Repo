//package com.farmted.boardservice.service;
//
//import com.farmted.boardservice.config.InitDB;
//import com.farmted.boardservice.domain.Board;
//import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
//import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
//import com.farmted.boardservice.dto.response.ResponseGetCombinationDetailDto;
//import com.farmted.boardservice.dto.response.ResponseGetCombinationListDto;
//import com.farmted.boardservice.enums.BoardType;
//import com.farmted.boardservice.exception.BoardException;
//import com.farmted.boardservice.repository.BoardRepository;
//import com.farmted.boardservice.util.Board1PageCache;
//import com.farmted.boardservice.vo.MemberVo;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.farmted.boardservice.config.InitDB.BOARD_UUIDS;
//import static com.farmted.boardservice.config.InitDB.MEMBER_UUID;
//import static com.farmted.boardservice.enums.BoardType.AUCTION;
//import static com.farmted.boardservice.enums.BoardType.SALE;
//import static java.util.concurrent.TimeUnit.MILLISECONDS;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.await;
//
//@SpringBootTest
//@ActiveProfiles({"test","testConfig"})
//@DisplayName("Board-Service 테스트 코드")
//class BoardServiceBootTest {
//    @Autowired
//    private Board1PageCache board1PageCache;
//    @Autowired
//    private BoardRepository boardRepository;
//    @Autowired
//    private BoardService boardService;
//
//    @Test
//    @Transactional
//    @DisplayName("게시글 생성")
//    void createBoard() {
//        // given
//        String createUuid = "createUuid";
//        MemberVo memberInfo = new MemberVo("회원명", "프로필URL");
//        RequestCreateBoardDto createBoardDto = new RequestCreateBoardDto(
//                AUCTION,             // BoardType 값
//                "게시글 내용",                  // 게시글 내용
//                "게시글 제목",                  // 게시글 제목
//                "상품 이름",                    // 상품 이름
//                10,                             // 상품 재고
//                10_000L,                         // 상품 가격
//                "상품 출처"                   // 상품 출처
//                );
//        // when
//        String boardUuid = boardService.createBoard(createBoardDto, createUuid, memberInfo);
//        // then
//            // createUuid로 조회되는 게시글 1개
//        assertThat(
//                boardRepository.findAll()
//                        .stream()
//                        .filter(board -> board.getMemberUuid().equals(createUuid))
//                        .toList()
//                        .get(0).getBoardUuid()
//        ).isEqualTo(boardUuid);
//    }
//
//    @Test
//    @DisplayName("전체 게시글 카테고리별 리스트 조회")
//    void getBoardList() {
//        // given
//        int pageNo = 1;
//        // page1에 값이 생길 때까지 최대 1.5초 대기
//        await().atMost(1500, MILLISECONDS).untilAsserted(
//                () -> {
//                    assertThat(board1PageCache.getPage1()).isNotNull();
//                    assertThat(board1PageCache.getPage1().getTotalElements()).isGreaterThan(1);
//                });
//        // when
//        for (BoardType category : BoardType.values()) {
//            int check = BoardType.PRODUCT.equals(category) ? 2 : 1;
//            // 카테고리별로 1개씩 저장했으니 PRODUCT(판매+경매 = 2개)를 제외한 모든 카테고리가 1이 나와야함.
//            ResponseGetCombinationListDto combiDTO = boardService.getBoardList(category, pageNo - 1);
//            // then
//            assertThat(combiDTO.getBoardList().size()).isEqualTo(check);
//        }
//
//    }
//
//    @Test
//    @DisplayName("작성자 글 카테고리별 리스트 조회")
//    void getWriterBoardList() {
//        // given
//        BoardType category = AUCTION;
//        int pageNo = 1;
//        String sellerUuid = InitDB.MEMBER_UUID;
//        // when
//        ResponseGetCombinationListDto combiListDTO =
//                boardService.getWriterBoardList(category, pageNo-1, sellerUuid);
//        // then
//        assertThat(combiListDTO.getBoardList().size()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("개별 경매 상품 상세 조회")
//    void getBoard() {
//        // given
//        // when
//        List<ResponseGetCombinationDetailDto> dtoList = new ArrayList<>();
//        for(String uuid : BOARD_UUIDS){
//            dtoList.add(boardService.getBoard(uuid));
//        }
//        // then
//        assertThat(dtoList.size()).isEqualTo(BOARD_UUIDS.size());
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("게시글 업데이트")
//    void updateBoard() {
//        // given
//        RequestUpdateProductBoardDto updateDTO = new RequestUpdateProductBoardDto(
//                SALE,           // BoardType 값
//                "수정된 게시글 내용",         // 수정된 게시글 내용
//                "수정된 게시글 제목",         // 수정된 게시글 제목
//                "수정된 상품 이름",           // 수정된 상품 이름
//                20,                           // 수정된 상품 재고
//                15_000L,                      // 수정된 상품 가격
//                "수정된 상품 출처",           // 수정된 상품 출처
//                "수정된 상품 이미지 URL"      // 수정된 상품 이미지 URL
//        );
//        // when
//        // then
//            // BoardType 변경의 경우, 판매와 경매(경매종료된)끼리의 변경만 가능
//            // == 요청한 updateDTO 타입이 SALE이었으니 SALE, PRODUCT만 가능
//        int cnt = 0;
//        for(String uuid : BOARD_UUIDS) {
//            try{
//                boolean updateCheck = boardService.updateBoard(updateDTO, uuid, MEMBER_UUID);
//                cnt++;
//            } catch (Exception e){
//                assertThat(e).isInstanceOf(BoardException.class);
//            }
//        }
//        assertThat(cnt).isEqualTo(2);
//    }
//
//
//    @Test
//    @Transactional
//    @DisplayName("게시글 삭제")
//    void deleteBoard() {
//        // given
//        // when
//        for(String uuid : BOARD_UUIDS){
//            boardService.deleteBoard(uuid, MEMBER_UUID);
//        }
//        // then
//        for (Board board : boardRepository.findAll()) {
//            assertThat(board.isBoardStatus()).isEqualTo(false);
//        }
//    }
//}