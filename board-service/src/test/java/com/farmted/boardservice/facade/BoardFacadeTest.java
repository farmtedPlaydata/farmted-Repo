package com.farmted.boardservice.facade;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.farmted.boardservice.config.InitDB.BOARD_UUIDS;
import static com.farmted.boardservice.config.InitDB.MEMBER_UUID;
import static com.farmted.boardservice.enums.BoardType.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles({"test","test-private"})
@TestMethodOrder(MethodOrderer.Random.class)
@DisplayName("Board-Service 통합 테스트 코드")
class BoardFacadeTest {
    // Feign통신 서비스
    @MockBean
    private MemberService memberService;
    @MockBean
    private ProductService productService;
    @MockBean
    private AuctionService auctionService;
    // S3 서비스
    @MockBean
    private ImageService imageService;
    // 가짜값이 없어도 되는 서비스
    @Autowired
    private BoardFacade boardFacade;

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private Board1PageCache board1PageCache;

    @Test
    @Transactional
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
            switch (category) {
                case AUCTION, SALE, CUSTOMER_SERVICE, COMMISSION ->
                    boardFacade.createBoard(new RequestCreateBoardDto(
                        category,             // BoardType 값
                        "게시글 내용",                  // 게시글 내용
                        "게시글 제목",                  // 게시글 제목
                        "상품 이름",                    // 상품 이름
                        10,                             // 상품 재고
                        10_000L,                         // 상품 가격
                        "상품 출처"                   // 상품 출처
                ), createUuid, role, image);
                case NOTICE ->
                    // Best case - 관리자인 경우
                    boardFacade.createBoard(new RequestCreateBoardDto(
                                BoardType.NOTICE,             // BoardType 값
                                "게시글 내용",                  // 게시글 내용
                                "게시글 제목",                  // 게시글 제목
                                "상품 이름",                    // 상품 이름
                                10,                             // 상품 재고
                                10_000L,                         // 상품 가격
                                "상품 출처"                    // 상품 출처
                    ), createUuid, RoleEnums.ADMIN);
                case PRODUCT -> {}
            }
        }
        // then
            // createUuid로 조회되는 게시글 5개 (Product 제외)
        assertThat(boardRepository.findAll().stream().filter(board -> board.getMemberUuid().equals(createUuid)).count()).isEqualTo(5);
            // 게시글 5개 저장 시도 -> 5회 호출
        verify(memberService, times(5)).getMemberInfo(eq(createUuid));
            // SALE, AUCTION -> 총 2회 호출
        verify(productService, times(2)).postProduct(any(ProductVo.class), eq(createUuid));
        verify(imageService, times(2)).uploadImageToS3(any(MultipartFile.class));
            // 역할이 게스트일 경우 글 작성 불가능
        Assertions.assertThrows(RoleTypeException.class,
                ()-> boardFacade.createBoard(new RequestCreateBoardDto(
                        COMMISSION,             // BoardType 값
                        "게시글 내용",                  // 게시글 내용
                        "게시글 제목",                  // 게시글 제목
                        "상품 이름",                    // 상품 이름
                        10,                             // 상품 재고
                        10_000L,                         // 상품 가격
                        "상품 출처"                   // 상품 출처
                ), createUuid, RoleEnums.GUEST, image));
    }

    @Test
    @Transactional
    @DisplayName("역할이 관리자가 아닌 경우 예외처리")
    void createNoticeUser(){
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
        // then
            // 관리자가 아닌 경우 RoleTypeException
        Assertions.assertThrows(
                RoleTypeException.class,
                () -> boardFacade.createBoard(new RequestCreateBoardDto(
                        BoardType.NOTICE,             // BoardType 값
                        "게시글 내용",                  // 게시글 내용
                        "게시글 제목",                  // 게시글 제목
                        "상품 이름",                    // 상품 이름
                        10,                             // 상품 재고
                        10_000L,                         // 상품 가격
                        "상품 출처"                    // 상품 출처
                ), createUuid, role)
        );
    }
    @Test
    @Transactional
    @DisplayName("올바르지 않은 카테고리로 요청(PRODUCT)한 경우")
    void createProductCategory() {
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
        // then
            // 올바르지 않은 생성 카테고리(PRODUCT)인 경우 BoardException
        Assertions.assertThrows(
                BoardException.class,
                () -> boardFacade.createBoard(new RequestCreateBoardDto(
                        BoardType.PRODUCT,             // BoardType 값
                        "게시글 내용",                  // 게시글 내용
                        "게시글 제목",                  // 게시글 제목
                        "상품 이름",                    // 상품 이름
                        10,                             // 상품 재고
                        10_000L,                         // 상품 가격
                        "상품 출처"                    // 상품 출처
                ), createUuid, role, ImageUtils.createTestImage("images/testJpg.jpg"))
        );
    }

    @Test
    @DisplayName("전체 게시글 카테고리별 리스트 조회")
    void getBoardList() {
        // given
        // page1에 값이 생길 때까지 최대 2초 대기
        await().atMost(2000, MILLISECONDS).untilAsserted(
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
            int check = BoardType.PRODUCT.equals(category) ?2 :1 ;
            // 카테고리별로 1개씩 저장했으니 PRODUCT(판매+경매 = 2개)를 제외한 모든 카테고리가 1이 나와야함.
            assertThat(boardFacade.getBoardList(category,pageNo-1).getBoardList().size())
                    .isEqualTo(check);
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
        ResponseGetCombinationListDto combiListDTO = boardFacade.getWriterBoardList(category, pageNo-1, sellerUuid);
        // then
        assertThat(combiListDTO.getBoardList().size()).isEqualTo(1);
        verify(productService, times(1)).getProductListByMember(eq(sellerUuid), eq(category), eq(pageNo-1));
        verify(auctionService, times(1)).getSellerAuctionList(eq(sellerUuid),eq(pageNo-1));
    }

    @Test
    @Transactional
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
            dtoList.add(boardFacade.getBoard(uuid));
        }
        // then
        assertThat(dtoList.size()).isEqualTo(BOARD_UUIDS.size());
        for (ResponseGetCombinationDetailDto dto : dtoList) {
            assertThat(dto.getBoardDetail().getViewCount()).isEqualTo(1);
        }
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
                boardFacade.updateBoard(updateDTO, uuid, MEMBER_UUID);
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
        doNothing().when(imageService).deleteImage(anyString());
        // when
        for(String uuid : BOARD_UUIDS){
            boardFacade.deleteBoard(uuid, MEMBER_UUID);
        }

        // then
        for (Board board : boardRepository.findAll()) {
            assertThat(board.isBoardStatus()).isEqualTo(false);
        }
        // 모든 카테고리 중 (SALE, AUCTION)의 경우만 상품 비활성화 가능
        verify(productService, times(2)).checkDeleteProduct(anyString(), eq(MEMBER_UUID));
        verify(imageService, times(2)).deleteImage(anyString());
    }
}