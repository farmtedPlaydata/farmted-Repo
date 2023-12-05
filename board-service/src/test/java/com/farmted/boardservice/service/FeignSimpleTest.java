package com.farmted.boardservice.service;

import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.feignClient.AuctionFeignClient;
import com.farmted.boardservice.feignClient.MemberFeignClient;
import com.farmted.boardservice.feignClient.ProductFeignClient;
import com.farmted.boardservice.repository.BoardRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Board-Service 테스트 코드")
public class FeignSimpleTest {
    private final Board1PageCache board1PageCache;
    private final NoticeService noticeService;
    private final BoardRepository boardRepository;

    private final ProductFeignClient productFeignClient = mock(ProductFeignClient.class);
    private final MemberFeignClient memberFeignClient = mock(MemberFeignClient.class);
    private final AuctionFeignClient auctionFeignClient = mock(AuctionFeignClient.class);

    private final FeignConverter<ProductVo> productConverter;
    private final FeignConverter<AuctionVo> auctionConverter;
    private final FeignConverter<MemberVo> memberConverter;

    private BoardService boardService;

    @Autowired
    public FeignSimpleTest(Board1PageCache board1PageCache, NoticeService noticeService, BoardRepository boardRepository, FeignConverter<ProductVo> productConverter, FeignConverter<AuctionVo> auctionConverter, FeignConverter<MemberVo> memberConverter) {
        this.board1PageCache = board1PageCache;
        this.noticeService = noticeService;
        this.boardRepository = boardRepository;
        this.productConverter = productConverter;
        this.auctionConverter = auctionConverter;
        this.memberConverter = memberConverter;
    }


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
        when(productFeignClient.createProductData(any(), any()))
                .thenReturn(ResponseEntity.ok(GlobalResponseDto.of(null)));
    // when
        boardService.createBoard(dummyData, uuid, role);
    // then
        verify(productFeignClient, times(1)).createProductData(any(), any());
        verify(memberFeignClient, times(1)).getMemberInfo(any());
        assertThat(boardRepository.findAll().size()).isEqualTo(6);
    }
}
