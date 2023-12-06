package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetAuctionDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetProductDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetAuctionDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.service.subService.AuctionService;
import com.farmted.boardservice.service.subService.MemberService;
import com.farmted.boardservice.service.subService.NoticeService;
import com.farmted.boardservice.service.subService.ProductService;
import com.farmted.boardservice.util.Board1PageCache;
import com.farmted.boardservice.vo.AuctionVo;
import com.farmted.boardservice.vo.MemberVo;
import com.farmted.boardservice.vo.ProductVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@DisplayName("Board-Service 테스트 코드")
class BoardServiceTest {
    private final BoardRepository boardRepository;
    private final NoticeService noticeService;

    @Autowired
    public BoardServiceTest(BoardRepository boardRepository, NoticeService noticeService) {
        this.boardRepository = boardRepository;
        this.noticeService = noticeService;
    }

    private final MemberService memberService = mock(MemberService.class);
    private final ProductService productService = mock(ProductService.class);
    private final AuctionService auctionService = mock(AuctionService.class);

    private List<String> boardUuid;
    private final String memberUuid = "member-uuid";
    private Board1PageCache board1PageCache;
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        // 레포 초기화
        boardUuid = new ArrayList<>();
        boardRepository.deleteAll();
        board1PageCache = new Board1PageCache(boardRepository);
        // board-service가 사용하는 하위 서비스를 mock객체로 만들어 주입
        boardService = new BoardService(boardRepository, board1PageCache, noticeService, productService, auctionService, memberService);

        // 더미데이터 생성
            // 카테고리별 저장
        for(BoardType category : BoardType.values()){
            if(BoardType.PRODUCT.equals(category)) continue;
            Board categoryBoard = new RequestCreateBoardDto(
                    category,             // BoardType 값
                    "게시글 내용",                  // 게시글 내용
                    category.getTypeKo(),          // 게시글 제목
                    "상품 이름",                    // 상품 이름
                    10,                             // 상품 재고
                    10_000L,                         // 상품 가격
                    "상품 출처",                    // 상품 출처
                    "상품 이미지 URL"               // 상품 이미지 URL
            ).toBoard(memberUuid, new MemberVo("member-name", "profile"));
            boardRepository.save(categoryBoard);
            boardUuid.add(categoryBoard.getBoardUuid());
        }
//        IntStream.rangeClosed(1, 5).forEach( (i)->{
//                    boardService.createBoard(new RequestCreateBoardDto(
//                            BoardType.AUCTION,             // BoardType 값
//                            "게시글 내용"+i,                  // 게시글 내용
//                            "게시글 제목"+i,                  // 게시글 제목
//                            "상품 이름"+i,                    // 상품 이름
//                            10*i,                             // 상품 재고
//                            10_000L*i,                         // 상품 가격
//                            "상품 출처"+i,                    // 상품 출처
//                            "상품 이미지 URL"+i               // 상품 이미지 URL
//                    ), "uuid"+i, RoleEnums.USER);
//                }
//        );
    }

    @Test
    @DisplayName("게시글 생성")
    void createBoard() {
        // given
        // when
        // then
    }

    @Test
    @DisplayName("전체 게시글 카테고리별 리스트 조회")
    void getBoardList() {
        // given
        int pageNo = 1;
        when(productService.getProductList(any(BoardType.class), eq(pageNo-1)))
                .thenReturn(List.of(new ResponseGetProductDto(
                        ProductVo.builder()
                                .productName("Name")
                                .productStock(100)
                                .productSource("Source")
                                .productImage("Image")
                                .boardUuid("boardUUID")
                                .boardType(BoardType.PRODUCT)
                                .productPrice(100_000L)
                                .build()))
                );
        when(auctionService.getAuctionList(eq(pageNo-1)))
                .thenReturn(List.of(new ResponseGetAuctionDto(
                        new AuctionVo(
                                500,                  // auctionPrice
                                "Dummy Buyer",        // auctionBuyer
                                LocalDateTime.now(), // auctionDeadline
                                true                  // auctionStatus
                        )
                )));
        await().atMost(2, SECONDS).untilAsserted(
                ()->assertThat(board1PageCache.getPage1()).isNotNull());
        // when
        // then
        for(BoardType category : BoardType.values()){
            int check = BoardType.PRODUCT.equals(category) ?2 :1 ;
            // 카테고리별로 1개씩 저장했으니 PRODUCT(판매+경매 = 2개)를 제외한 모든 카테고리가 1이 나와야함.
            assertThat(boardService.getBoardList(category, pageNo-1).getBoardList().getContent().size())
                    .isEqualTo(check);
        }
        // then
            // SALE, AUCTION, PRODUCT의 3번의 경우
        verify(productService, times(3)).getProductList(eq(any(BoardType.class)), eq(pageNo-1));
            // AUCTION 일때만
        verify(auctionService, times(1)).getAuctionList(eq(pageNo-1));
    }

    @Test
    @DisplayName("작성자 글 카테고리별 리스트 조회")
    void getWriterBoardList() {
        // given
        // when
        // then
    }

    @Test
    @DisplayName("개별 경매 상품 상세 조회")
    void getBoard() {
        // given
        when(productService.getProductByBoardUuid(anyString()))
                .thenReturn(new ResponseGetProductDetailDto(
                        "Dummy Product",
                        10,
                        10000L,
                        "Dummy Source",
                        "Dummy Image"
                ));
        when(auctionService.getAuctionDetail(anyString()))
                .thenReturn(new ResponseGetAuctionDetailDto(
                        500,       // auctionPrice
                        "Dummy Buyer",        // auctionBuyer
                        LocalDateTime.now(),  // auctionDeadline
                        true                  // auctionStatus
                ));
        // when
        List<ResponseGetCombinationDetailDto> dtoList = new ArrayList<>();
        for(String uuid : boardUuid){
            dtoList.add(boardService.getBoard(uuid));
        }
        // then
        assertThat(dtoList.size()).isEqualTo(boardUuid.size());
        verify(productService, times(2)).getProductByBoardUuid(anyString());
        verify(auctionService, times(1)).getAuctionDetail(anyString());
    }

    @Test
    @DisplayName("게시글 업데이트")
    void updateBoard() {
        // given
        RequestUpdateProductBoardDto updateDTO = new RequestUpdateProductBoardDto(
                BoardType.SALE,           // BoardType 값
                "수정된 게시글 내용",         // 수정된 게시글 내용
                "수정된 게시글 제목",         // 수정된 게시글 제목
                "수정된 상품 이름",           // 수정된 상품 이름
                20,                           // 수정된 상품 재고
                15_000L,                      // 수정된 상품 가격
                "수정된 상품 출처",           // 수정된 상품 출처
                "수정된 상품 이미지 URL"      // 수정된 상품 이미지 URL
        );
        doNothing().when(productService).checkUpdateProduct(anyString(), eq(updateDTO), eq(memberUuid));

        // when
        for(String uuid : boardUuid) {
            try{
                boardService.updateBoard(updateDTO, uuid, memberUuid);
            }catch (Exception e){
                // Exception Pass 예외검사 then에서 할 예정
            }
        }
        // then
            // BoardType 변경의 경우, 판매와 경매(경매종료된)끼리의 변경만 가능
        for (Board board : boardRepository.findAll()){
            switch (board.getBoardType()){
                case NOTICE, COMMISSION, CUSTOMER_SERVICE
                        -> assertThat(board.getBoardTitle())
                        .isNotEqualTo(updateDTO.boardTitle());
                case SALE, AUCTION
                        -> assertThat(board.getBoardTitle())
                        .isEqualTo(updateDTO.boardTitle());
            }
        }
         // 모든 카테고리 중 (SALE, AUCTION)의 경우만 상품 비활성화 가능
        verify(productService, times(2)).checkUpdateProduct(anyString(), eq(updateDTO), eq(memberUuid));
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteBoard() {
        // given
            // 반환값이 void
        doNothing().when(productService).checkDeleteProduct(anyString(), eq(memberUuid));

        // when
        for(String uuid : boardUuid){
            boardService.deleteBoard(uuid, memberUuid);
        }

        // then
        for (Board board : boardRepository.findAll()) {
            assertThat(board.isBoardStatus()).isEqualTo(false);
        }
            // 모든 카테고리 중 (SALE, AUCTION)의 경우만 상품 비활성화 가능
        verify(productService, times(2)).checkDeleteProduct(anyString(), eq(memberUuid));
    }
}