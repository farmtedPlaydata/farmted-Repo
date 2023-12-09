package com.farmted.boardservice.demoTest;

import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.feignClient.AuctionFeignClient;
import com.farmted.boardservice.feignClient.MemberFeignClient;
import com.farmted.boardservice.feignClient.ProductFeignClient;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.service.BoardService;
import com.farmted.boardservice.service.subService.AuctionService;
import com.farmted.boardservice.service.subService.MemberService;
import com.farmted.boardservice.service.subService.NoticeService;
import com.farmted.boardservice.service.subService.ProductService;
import com.farmted.boardservice.util.Board1PageCache;
import com.farmted.boardservice.util.GlobalResponseDto;
import com.farmted.boardservice.util.feignConverter.FeignConverter;
import com.farmted.boardservice.vo.AuctionVo;
import com.farmted.boardservice.vo.MemberVo;
import com.farmted.boardservice.vo.ProductVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Feign Mock객체 실험")
public class MockFeignTest {
    @Spy
    private BoardRepository boardRepository;
    @Spy
    private Board1PageCache board1PageCache;
    @Spy
    private NoticeService noticeService;

    @Mock
    private ProductFeignClient productFeignClient;
    @Mock
    private MemberFeignClient memberFeignClient;
    @Mock
    private AuctionFeignClient auctionFeignClient;

    @Spy
    private FeignConverter<ProductVo> productConverter;
    @Spy
    private FeignConverter<AuctionVo> auctionConverter;
    @Spy
    private FeignConverter<MemberVo> memberConverter;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    void setUp() {
    // 레포지터리 초기화
        boardRepository.deleteAll();
    // Mock 객체인 Feign Client 주입
        ProductService productService = new ProductService(productFeignClient, productConverter);
        AuctionService auctionService = new AuctionService(auctionFeignClient, auctionConverter);
        MemberService memberService = new MemberService(memberFeignClient, memberConverter);
    // 주입된 각 서비스 주입
        boardService = new BoardService(boardRepository,board1PageCache,noticeService, productService, auctionService, memberService);

    // 더미데이터 생성
        IntStream.rangeClosed(1, 5).forEach( (i)->{
                    boardService.createBoard(new RequestCreateBoardDto(
                            BoardType.AUCTION,             // BoardType 값
                            "게시글 내용"+i,                  // 게시글 내용
                            "게시글 제목"+i,                  // 게시글 제목
                            "상품 이름"+i,                    // 상품 이름
                            10*i,                             // 상품 재고
                            10_000L*i,                         // 상품 가격
                            "상품 출처"+i,                    // 상품 출처
                            "상품 이미지 URL"+i               // 상품 이미지 URL
                    ), "uuid"+i, RoleEnums.USER);
                }
        );
    }
    
    @Test
    @DisplayName("게시글 생성")
    public void createBoard() {
    // given
        // member UUID, Role 세팅
        String uuid = "userUUID";
        RoleEnums role = RoleEnums.USER;
        // 더미데이터 생성
        RequestCreateBoardDto dummyData = new RequestCreateBoardDto(
                BoardType.SALE,             // BoardType 값
                "게시글 내용",                  // 게시글 내용
                "Example",                  // 게시글 제목
                "상품 이름",                    // 상품 이름
                10,                             // 상품 재고
                10_000L,                         // 상품 가격
                "상품 출처",                    // 상품 출처
                "상품 이미지 URL"               // 상품 이미지 URL
        );
        // Feign 값 선언
        when(memberFeignClient.getMemberInfo(any()))
                .thenReturn(ResponseEntity.ok(GlobalResponseDto.of(new MemberVo(uuid, "유저에옹"))));
        when(productFeignClient.createProductData(any(), uuid))
                .thenReturn(ResponseEntity.ok(GlobalResponseDto.of(null)));
    // when
        boardService.createBoard(dummyData, uuid, role);
    // then
        verify(productFeignClient, times(1)).createProductData(any(), uuid);
        verify(memberFeignClient, times(1)).getMemberInfo(any());
        assertThat(boardRepository.findAll().size()).isEqualTo(6);
    }
}
