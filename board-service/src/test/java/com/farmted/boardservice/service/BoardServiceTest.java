package com.farmted.boardservice.service;

import com.farmted.boardservice.config.ImageUtils;
import com.farmted.boardservice.config.InitDB;
import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationDetailDto;
import com.farmted.boardservice.dto.response.ResponseGetCombinationListDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetAuctionDetailDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetProductDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetAuctionDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.BoardException;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.service.subService.AuctionService;
import com.farmted.boardservice.service.subService.ImageService;
import com.farmted.boardservice.service.subService.MemberService;
import com.farmted.boardservice.service.subService.ProductService;
import com.farmted.boardservice.util.Board1PageCache;
import com.farmted.boardservice.vo.AuctionVo;
import com.farmted.boardservice.vo.MemberVo;
import com.farmted.boardservice.vo.ProductVo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.farmted.boardservice.config.InitDB.BOARD_UUIDS;
import static com.farmted.boardservice.config.InitDB.MEMBER_UUID;
import static com.farmted.boardservice.enums.BoardType.AUCTION;
import static com.farmted.boardservice.enums.BoardType.SALE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles({"test","testConfig"})
@TestMethodOrder(MethodOrderer.Random.class)
@DisplayName("Board-Service 테스트 코드")
class BoardServiceTest {
    @MockBean
    private MemberService memberService;
    @MockBean
    private ProductService productService;
    @MockBean
    private AuctionService auctionService;
    @MockBean
    private ImageService imageService;

    @Autowired
    private Board1PageCache board1PageCache;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("게시글 생성")
    void createBoard() {
        // given
        String createUuid = "createUuid";
        RoleEnums role = RoleEnums.USER;
        MultipartFile image = ImageUtils.createTestImage("images/testJpg.jpg");
        when(memberService.getMemberInfo(eq(createUuid)))
                .thenReturn(new MemberVo("회원명", "프로필URL"));
        when(imageService.uploadImageToS3(image))
                .thenReturn("imageURL");
        doNothing().when(productService).postProduct(any(ProductVo.class), eq(createUuid));
        // when
        for (BoardType category : BoardType.values()) {
            switch (category){
                case NOTICE, PRODUCT -> {}
                case SALE, COMMISSION, AUCTION, CUSTOMER_SERVICE -> {
                    if(!(category == SALE || category == AUCTION)) image = null;
                    else image = ImageUtils.createTestImage("images/testJpg.jpg");
                    boardService.createBoard(new RequestCreateBoardDto(
                            category,             // BoardType 값
                            "게시글 내용",                  // 게시글 내용
                            "게시글 제목",                  // 게시글 제목
                            "상품 이름",                    // 상품 이름
                            10,                             // 상품 재고
                            10_000L,                         // 상품 가격
                            "상품 출처"                   // 상품 출처
                    ), createUuid, role, image);
                }
            }
        }
        // then
            // createUuid로 조회되는 게시글 4개 (Product, Notice 제외)
        assertThat(boardRepository.findAll().stream().filter(board -> board.getMemberUuid().equals(createUuid)).count()).isEqualTo(4);
            // 게시글 4개 저장했으니 4회 호출
        verify(memberService, times(4)).getMemberInfo(eq(createUuid));
            // SALE, AUCTION -> 총 2회 호출
        verify(productService, times(2)).postProduct(any(ProductVo.class), eq(createUuid));
        verify(imageService, times(2)).uploadImageToS3(any(MultipartFile.class));
            // 관리자가 아닌 경우 RoleTypeException
        Assertions.assertThrows(
                RoleTypeException.class,
                ()->boardService.createBoard(new RequestCreateBoardDto(
                        BoardType.NOTICE,             // BoardType 값
                        "게시글 내용",                  // 게시글 내용
                        "게시글 제목",                  // 게시글 제목
                        "상품 이름",                    // 상품 이름
                        10,                             // 상품 재고
                        10_000L,                         // 상품 가격
                        "상품 출처"                    // 상품 출처
                ), createUuid, role));
            // 올바르지 않은 생성 카테고리(PRODUCT)인 경우 BoardException
        Assertions.assertThrows(
                BoardException.class,
                ()->boardService.createBoard(new RequestCreateBoardDto(
                        BoardType.PRODUCT,             // BoardType 값
                        "게시글 내용",                  // 게시글 내용
                        "게시글 제목",                  // 게시글 제목
                        "상품 이름",                    // 상품 이름
                        10,                             // 상품 재고
                        10_000L,                         // 상품 가격
                        "상품 출처"                    // 상품 출처
                ), createUuid, role, ImageUtils.createTestImage("images/testJpg.jpg")));
    }

    @Test
    @DisplayName("전체 게시글 카테고리별 리스트 조회")
    void getBoardList() {
        // given
        // page1에 값이 생길 때까지 최대 2초 대기
        await().atMost(1500, MILLISECONDS).untilAsserted(
                () -> {
                    assertThat(board1PageCache.getPage1()).isNotNull();
                    assertThat(board1PageCache.getPage1().getTotalElements()).isGreaterThan(1);
                });
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
        // when
        for(BoardType category : BoardType.values()){
            System.out.println("$$$"+category);
            int check = BoardType.PRODUCT.equals(category) ?2 :1 ;
            // 카테고리별로 1개씩 저장했으니 PRODUCT(판매+경매 = 2개)를 제외한 모든 카테고리가 1이 나와야함.
                // 1초마다 작동하는 스케줄러가 작동하고 있어 최대 1.5초 대기
            boardService.getBoardList(category,pageNo-1);
        }
        // then
            // SALE, AUCTION, PRODUCT의 3번의 경우
        verify(productService, times(3)).getProductList(any(BoardType.class), eq(pageNo-1));
            // AUCTION 일때만
        verify(auctionService, times(1)).getAuctionList(eq(pageNo-1));
    }

    @Test
    @DisplayName("작성자 글 카테고리별 리스트 조회")
    void getWriterBoardList() {
        // given
        BoardType category = AUCTION;
        int pageNo = 1;
        String sellerUuid = InitDB.MEMBER_UUID;
        when(productService.getProductListByMember(eq(sellerUuid), eq(category), eq(pageNo-1)))
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
        when(auctionService.getSellerAuctionList(eq(sellerUuid),eq(pageNo-1)))
                .thenReturn(List.of(new ResponseGetAuctionDto(
                        new AuctionVo(
                                500,                  // auctionPrice
                                "Dummy Buyer",        // auctionBuyer
                                LocalDateTime.now(), // auctionDeadline
                                true                  // auctionStatus
                        )
                )));
        // when
        ResponseGetCombinationListDto combiListDTO = boardService.getWriterBoardList(category, pageNo-1, sellerUuid);
        // then
        assertThat(combiListDTO.getBoardList().size()).isEqualTo(1);
        verify(productService, times(1)).getProductListByMember(eq(sellerUuid), eq(category), eq(pageNo-1));
        verify(auctionService, times(1)).getSellerAuctionList(eq(sellerUuid),eq(pageNo-1));
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
        for(String uuid : BOARD_UUIDS){
            dtoList.add(boardService.getBoard(uuid));
        }
        // then
        assertThat(dtoList.size()).isEqualTo(BOARD_UUIDS.size());
        verify(productService, times(2)).getProductByBoardUuid(anyString());
        verify(auctionService, times(1)).getAuctionDetail(anyString());
    }

    @Test
    @Transactional
    @DisplayName("게시글 업데이트")
    void updateBoard() {
        // given
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
        doNothing().when(productService).checkUpdateProduct(anyString(), eq(updateDTO), eq(MEMBER_UUID));

        // when
        for(String uuid : BOARD_UUIDS) {
            try{
                boardService.updateBoard(updateDTO, uuid, MEMBER_UUID);
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
        verify(productService, times(2)).checkUpdateProduct(anyString(), eq(updateDTO), eq(MEMBER_UUID));
    }

    @Test
    @Transactional
    @DisplayName("게시글 삭제")
    void deleteBoard() {
        // given
            // 반환값이 void
        doNothing().when(productService).checkDeleteProduct(anyString(), eq(MEMBER_UUID));

        // when
        for(String uuid : BOARD_UUIDS){
            boardService.deleteBoard(uuid, MEMBER_UUID);
        }

        // then
        for (Board board : boardRepository.findAll()) {
            assertThat(board.isBoardStatus()).isEqualTo(false);
        }
            // 모든 카테고리 중 (SALE, AUCTION)의 경우만 상품 비활성화 가능
        verify(productService, times(2)).checkDeleteProduct(anyString(), eq(MEMBER_UUID));
    }
}