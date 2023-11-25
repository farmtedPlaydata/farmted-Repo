package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateProductBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetBoardDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.exception.RoleTypeException;
import com.farmted.boardservice.feignClient.ProductFeignClient;
import com.farmted.boardservice.repository.BoardRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

            // Test코드 환경의 포트 번호를 랜덤으로 실행
@SpringBootTest
    // 스케줄러 cron값 임의 변경 가능하도록
//@TestPropertySource(properties = "schedules.cron= * * * * * *")
@DisplayName("경매 게시글 테스트 코드")
@Transactional
public class ServiceTest {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final ProductFeignClient productFeignClient;

    static final int PORT = 8080;
    public static WireMockServer wireMockServer = new WireMockServer(options().port(PORT));


    @Autowired
    public ServiceTest(BoardService boardService, BoardRepository boardRepository, ProductFeignClient productFeignClient) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
        this.productFeignClient = productFeignClient;
    }

    // 어디로 Feign 통신할 지 선언
    @DynamicPropertySource
    public static void addUrlProperties(DynamicPropertyRegistry registry) {
        registry.add("product-service", () -> "localhost:" + PORT);
    }

    // 테스트 코드 시작 전에 서버 켜기
    @BeforeAll
    public static void beforeAll() {
        wireMockServer.start();
        WireMock.configureFor("localhost", PORT);
    }
    // 테스트 코드 끝나면 서버 끄기
    @AfterAll
    public static void afterAll() {
        wireMockServer.stop();
    }
    // 각 테스트가 끝날 때 서버 초기화
    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    // 레포 초기화 및 더미데이터 생성
    @BeforeEach
    public void beforeEach(){
        boardRepository.deleteAll();
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

    @Test
    @DisplayName("상품 게시글 생성")
    public void createBoardTest(){
        // given
            // WireMock에게 예상되는 HTTP 요청 및 응답을 설정
        stubFor(post(urlEqualTo("/product-service/products/boards"))
                .willReturn(aResponse().withStatus(200)));
            // member UUID, Role 세팅
        String uuid = "userUUID";
        RoleEnums role = RoleEnums.USER;
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
        boardService.createActionBoard(dummyData, uuid, role.toString());

        // then
            // wireMock에게 예상대로 요청이 왔는지 확인
        verify(postRequestedFor(urlEqualTo("/product-service/products/boards"))
                .withHeader("Content-Type", equalTo("application/json")));
    }
    // Create 예외 확인 로직
        // 주어진 Role이 존재하지 않는 값인 경우
    @Test
    @DisplayName("상품 게시글 생성 - Role이 존재하지 않는 값인 경우")
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
    @DisplayName("상품 게시글 생성 - ROLE_GUEST인 경우")
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
    @DisplayName("경매 게시글 조회 - 1페이징 캐시")
    public void getAuctionBoardPage1() throws InterruptedException {
        // given
        int page = 1;

        // when
        // then
            // 1초 단위 스케줄러 실행을 위해 2초 대기
        System.out.println("@@@ + "+ boardRepository.findByBoardTypeAndBoardStatus(BoardType.AUCTION, true,
                        PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createAt")))
                .map(ResponseGetBoardDto::new).getContent());
        Awaitility.await().atMost(5000, TimeUnit.MILLISECONDS)
                .until( ()-> {
                            // 여기서 스케줄러에 의해 업데이트된 값을 가져와서 검증
                            Page<ResponseGetBoardDto> responseDto = boardService.getAuctionBoardList(page - 1);
                            return responseDto.getContent().size() == 3;
                });
        System.out.println("####"+boardRepository.findAll().size());
    }
        // 1페이지가 아닌 경우
    @Test
    @DisplayName("경매 게시글 조회 - 2페이지 이상")
    public void getAuctionBoardList(){
        // given
        int page = 2;
        // when
        Page<ResponseGetBoardDto> responseDto = boardService.getAuctionBoardList(page-1);

        // then
        assertThat(responseDto.getContent().size()).isEqualTo(2);
    }

    // 개별 경매 게시글 조회
    @Test
    @DisplayName("경매 게시글 개별 조회")
    public void getAuctionBoard(){
        // given
        // 더미데이터 생성
        Board board = boardRepository.findAll().get(0);

        // when
        ResponseGetBoardDetailDto responseDto = boardService.getAuctionBoard(board.getBoardUuID());

        // then
        assertThat(responseDto.getBoardTitle()).isEqualTo(board.getBoardTitle());
    }

    @Test
    @DisplayName("경매 게시글 삭제")
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

    @Test
    @DisplayName("경매 게시글 조회 - 입력받은 게시글UUID가 유효하지 않음")
    public void deleteAuctionBoardWithWeirdBoardUuid(){
        // given
        String boardUuid = "123";
        String uuid = "uuid1";
        // when
        // then
        Assertions.assertThrows(RuntimeException.class, ()-> boardService.deleteAuctionBoard(boardUuid, uuid));
    }

    @Test
    @DisplayName("경매 게시글 업데이트")
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
