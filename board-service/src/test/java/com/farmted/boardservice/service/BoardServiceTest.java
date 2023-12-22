package com.farmted.boardservice.service;

import com.farmted.boardservice.config.InitDB;
import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationDetailDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationListDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetBoardDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.exception.BoardException;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.util.Board1PageCache;
import com.farmted.boardservice.vo.MemberVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.farmted.boardservice.enums.BoardType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import({Board1PageCache.class, BoardService.class, BoardRepository.class})
@DisplayName("Board-Service 테스트 코드")
class BoardServiceTest {
    @Mock
    Board1PageCache board1PageCache;
    @Mock
    private BoardRepository boardRepository;
    @InjectMocks
    private BoardService boardService;

    // 조회용 더미데이터
    static List<ResponseGetBoardDto> DUMMY_DATA_LIST;
    static Page<ResponseGetBoardDto> DUMMY_PAGE;
    static ResponseGetBoardDetailDto DUMMY_DATA;
    static Board DUMMY_BOARD;
    static List<Board> DUMMY_BOARD_LIST = new ArrayList<>();
    static String DUMMY_BOARD_UUID;
    static Pageable PAGE_INFO;

    @BeforeAll
    static void beforeAll() {
        String memberUuid = "member-uuid";
        for(BoardType category : BoardType.values()){
            if (category.equals(PRODUCT)) continue;
            DUMMY_BOARD_LIST.add( Board.builder()
                    .boardType(category) // 무작위 값으로 변경
                    .boardTitle("Random Board Title")
                    .boardContent("Random Board Content")
                    .viewCount(0) // 초기값
                    .boardStatus(true) // 초기값
                    .memberName("Random Member Name")
                    .memberProfile("Random Member Profile")
                    .memberUuid(memberUuid) // 무작위 UUID
                    .boardUuid(UUID.randomUUID().toString()) // 무작위 UUID
                    .build()
            );
        }
        DUMMY_BOARD = DUMMY_BOARD_LIST.get(0);
        ResponseGetBoardDto DUMMY_DTO = new ResponseGetBoardDto(DUMMY_BOARD);
        PAGE_INFO = PageRequest.of(0, 3);
        DUMMY_DATA_LIST = Arrays.asList(DUMMY_DTO, DUMMY_DTO);
        DUMMY_PAGE = new PageImpl<>(DUMMY_DATA_LIST, PAGE_INFO, DUMMY_DATA_LIST.size());
        DUMMY_DATA = new ResponseGetBoardDetailDto(DUMMY_BOARD);
        DUMMY_BOARD_UUID = DUMMY_BOARD.getBoardUuid();
    }

    @Test
    @Transactional
    @DisplayName("게시글 생성")
    void createBoard() {
        // given
        String createUuid = "createUuid";
        MemberVo memberInfo = new MemberVo("회원명", "프로필URL");
        RequestCreateBoardDto createBoardDto = new RequestCreateBoardDto(
                AUCTION,             // BoardType 값
                "게시글 내용",                  // 게시글 내용
                "게시글 제목",                  // 게시글 제목
                "상품 이름",                    // 상품 이름
                10,                             // 상품 재고
                10_000L,                         // 상품 가격
                "상품 출처"                   // 상품 출처
                );
        Board saveBoard = createBoardDto.toBoard(createUuid, memberInfo);
        when(boardRepository.save(any(Board.class)))
                .thenReturn(createBoardDto.toBoard(createUuid, memberInfo));
        // when
        String boardUuid = boardService.createBoard(createBoardDto, createUuid, memberInfo);
        // then
            // createUuid로 조회되는 게시글 1개
        assertThat(saveBoard.getBoardUuid()).isEqualTo(boardUuid);
    }

    @Test
    @DisplayName("전체 게시글 카테고리별 리스트 조회")
    void getBoardList() {
        // given
        int pageNo = 1;
        when(board1PageCache.getPage1()).thenReturn(DUMMY_PAGE);
        when(boardRepository.findByBoardType(any(BoardType.class), any(Pageable.class)))
                .thenReturn(DUMMY_PAGE);
        // when
        for (BoardType category : BoardType.values()) {
            ResponseGetCombinationListDto combiDTO = boardService.getBoardList(category, pageNo - 1);
        // then
            assertThat(combiDTO.getBoardList().size()).isEqualTo(2);
        }
        verify(board1PageCache, times(1)).getPage1();
            // Product 이외의 카테고리는 레포가 조회 (5회)
        verify(boardRepository, times(5)).findByBoardType(any(BoardType.class), any(Pageable.class));
    }

    @Test
    @DisplayName("작성자 글 카테고리별 리스트 조회")
    void getWriterBoardList() {
        // given
        BoardType category = AUCTION;
        int pageNo = 1;
        String sellerUuid = InitDB.MEMBER_UUID;
        when(boardRepository.findByMemberUuidAndBoardType(eq(sellerUuid), eq(category), any(Pageable.class)))
                .thenReturn(DUMMY_PAGE);
        // when
        ResponseGetCombinationListDto combiListDTO =
                boardService.getWriterBoardList(category, pageNo-1, sellerUuid);
        // then
        assertThat(combiListDTO.getBoardList().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("개별 경매 상품 상세 조회")
    void getBoard() {
        // given
        String boardUuid = "board_uuid";
        when(boardRepository.findDetailByBoardUuid(anyString()))
                .thenReturn(Optional.of(DUMMY_DATA));
        // when
        ResponseGetCombinationDetailDto detailDTO = boardService.getBoard(boardUuid);

        // then
        assertThat(detailDTO.getBoardDetail().getBoardUuid()).isEqualTo(DUMMY_BOARD_UUID);
    }

    @Test
    @Transactional
    @DisplayName("게시글 업데이트 - 상품 게시글의 경우")
    void updateBoardSALE() {
        // given
        String memberUuid = "member-uuid";
        RequestUpdateProductBoardDto updateDTO = new RequestUpdateProductBoardDto(
                SALE,           // BoardType 값
                "수정된 게시글 내용",         // 수정된 게시글 내용
                "수정된 게시글 제목",         // 수정된 게시글 제목
                "수정된 상품 이름",           // 수정된 상품 이름
                20,                           // 수정된 상품 재고
                15_000L,                      // 수정된 상품 가격
                "수정된 상품 출처",           // 수정된 상품 출처
                "수정된 상품 이미지 URL"      // 수정된 상품 이미지 URL
        );
        DUMMY_BOARD_LIST
                .forEach(board ->
                        when(boardRepository.findByBoardUuidAndBoardStatusTrue(DUMMY_BOARD_UUID))
                                .thenReturn(Optional.of(board))
                );
        // when
        for(Board board : DUMMY_BOARD_LIST){
            try{
                boolean updateCheck = boardService.updateBoard(updateDTO, DUMMY_BOARD_UUID, memberUuid);
                switch (board.getBoardType()){
        // then
            // BoardType 변경의 경우, 판매와 경매(경매종료된)끼리의 변경만 가능
                    case NOTICE, COMMISSION, CUSTOMER_SERVICE
                            -> assertThat(updateCheck)
                            .isEqualTo(false);
                    case SALE, AUCTION
                            -> assertThat(updateCheck)
                            .isEqualTo(true);
                }
            } catch (Exception e){
                assertThat(e).isInstanceOf(BoardException.class);
            }
        }
    }

    @Test
    @Transactional
    @DisplayName("게시글 업데이트 - 일반 게시글의 경우")
    void updateBoardCOMMISTION() {
        // given
        String memberUuid = "member-uuid";
        RequestUpdateProductBoardDto updateDTO = new RequestUpdateProductBoardDto(
                COMMISSION,           // BoardType 값
                "수정된 게시글 내용",         // 수정된 게시글 내용
                "수정된 게시글 제목",         // 수정된 게시글 제목
                "수정된 상품 이름",           // 수정된 상품 이름
                20,                           // 수정된 상품 재고
                15_000L,                      // 수정된 상품 가격
                "수정된 상품 출처",           // 수정된 상품 출처
                "수정된 상품 이미지 URL"      // 수정된 상품 이미지 URL
        );
        DUMMY_BOARD_LIST
                .forEach(board ->
                        when(boardRepository.findByBoardUuidAndBoardStatusTrue(board.getBoardUuid()))
                                .thenReturn(Optional.of(board))
                );
        // when
        for(Board board : DUMMY_BOARD_LIST){
            try{
                boolean updateCheck = boardService.updateBoard(updateDTO, board.getBoardUuid(), memberUuid);
                // then
                // BoardType 변경의 경우, 판매와 경매(경매종료된)끼리의 변경만 가능
                if (COMMISSION.equals(board.getBoardType())) {
                    assertThat(updateCheck)
                            .isEqualTo(false);
                }
            } catch (Exception e){
                assertThat(e).isInstanceOf(BoardException.class);
            }
        }
    }

    @Test
    @Transactional
    @DisplayName("게시글 삭제")
    void deleteBoard() {
        // given
        String memberUUID = "member-uuid";
        DUMMY_BOARD_LIST
                .forEach(board ->
                        when(boardRepository.findByBoardUuidAndBoardStatusTrue(board.getBoardUuid()))
                                .thenReturn(Optional.of(board))
                );
        // 예외 확인용 BoardUuid
        // when
        for(Board board : DUMMY_BOARD_LIST){
            // 하나는 Exception 확인용으로 실패
            if(Objects.equals(board.getBoardUuid(), DUMMY_BOARD_UUID)){
                Assertions.assertThrows(BoardException.class,
                    () -> boardService.deleteBoard(board.getBoardUuid(), "123"));
                continue;
            }
            boardService.deleteBoard(board.getBoardUuid(), memberUUID);
        }
        // then
        for(Board board : DUMMY_BOARD_LIST) {
            if(Objects.equals(board.getBoardUuid(), DUMMY_BOARD_UUID)) continue;
            assertThat(board.isBoardStatus()).isEqualTo(false);
        }
    }
}