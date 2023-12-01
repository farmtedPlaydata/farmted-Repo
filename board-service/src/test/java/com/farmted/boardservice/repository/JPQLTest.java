package com.farmted.boardservice.repository;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.enums.BoardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("JPQL 테스트 코드")
@Transactional
class JPQLTest {
    private final BoardRepository boardRepository;

    @Autowired
    JPQLTest(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 레포 초기화 및 더미데이터 생성
    @BeforeEach
    void beforeEach() {
        boardRepository.deleteAll();
        // 경매 생성
        boardRepository.save(new RequestCreateBoardDto(
                BoardType.AUCTION,
                "Auction Content",
                "Auction Title",
                "Auction Product",
                10,
                100,
                "Auction Source",
                "Auction Image"
        ).toBoard("uuid", "member"));
        // 판매 생성
        boardRepository.save(new RequestCreateBoardDto(
                BoardType.SALE,
                "Sale Content",
                "Sale Title",
                "Sale Product",
                20,
                200,
                "Sale Source",
                "Sale Image"
        ).toBoard("uuid", "member"));
        // 구매요청 생성
        boardRepository.save(new RequestCreateBoardDto(
                BoardType.COMMISSION,
                "Commission Content",
                "Commission Title",
                "", 0, 0, "", ""
        ).toBoard("uuid", "member"));
        // 그 이외의 더미데이터 (고객센터로 고정)
        IntStream.rangeClosed(1, 5).forEach((i) -> {
                    boardRepository.save(new RequestCreateBoardDto(
                            BoardType.CUSTOMER_SERVICE,      // BoardType 값
                            "게시글 내용" + i,                  // 게시글 내용
                            "게시글 제목" + i,                  // 게시글 제목
                            "상품 이름" + i,                    // 상품 이름
                            10 * i,                             // 상품 재고
                            10_000L * i,                         // 상품 가격
                            "상품 출처" + i,                    // 상품 출처
                            "상품 이미지 URL" + i               // 상품 이미지 URL
                    ).toBoard("DummuUuid" + i, "member" + i));
                }
        );
    }

    @Test
    @DisplayName("Product(판매+경매) 게시글 조회")
    void GetBoardByProductTest() {
        // given
        // when
        Page<ResponseGetBoardDto> boardDTO = boardRepository.findByBoardType(BoardType.PRODUCT,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
        // then
        assertThat(boardDTO.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("카테고리별 게시글 조회")
    void GetBoardByCategoryTest() {
        // given
        // when
        Page<ResponseGetBoardDto> boardDTO = boardRepository.findByBoardType(BoardType.COMMISSION,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
        // then
        assertThat(boardDTO.getContent().size()).isEqualTo(1);
        assertThat(boardDTO.getContent().get(0).getBoardType()).isEqualTo(BoardType.COMMISSION);
    }

    @Test
    @DisplayName("Product 특정 회원의 게시글 리스트")
    void GetMemberBoardByProductTest() {
        // given
        // when
        // 회원UUID가 "uuid"인 게시글
        Page<ResponseGetBoardDto> boardDTO = boardRepository.findByMemberUuidAndBoardType("uuid", BoardType.PRODUCT,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
        // then
        assertThat(boardDTO.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("카테고리별 특정 회원의 게시글 리스트")
    void GetMemberBoardByCategory() {
        // given
        // when
            // 회원UUID가 "uuid"인 게시글 중 SALE인 게시글
        Page<ResponseGetBoardDto> boardDTO = boardRepository.findByMemberUuidAndBoardType("uuid", BoardType.SALE,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
        // then
        assertThat(boardDTO.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void GetBoardDetail(){
        // given
            // 사전 데이터
        String memberUuid = "uuidDetail";
        RequestCreateBoardDto createBoardDto = new RequestCreateBoardDto(
                BoardType.AUCTION,
                "Auction Content",
                "상세 조회 확인 글",
                "Auction Product",
                10,
                100,
                "Auction Source",
                "Auction Image"
        );
            // 저장
        Board board = createBoardDto.toBoard(memberUuid, "memberDetail");
        boardRepository.save(board);
        String boardUuid = board.getBoardUuid();
        // 비교할 데이터 준비
        String boardTitle = createBoardDto.boardTitle();

        // when
        String getBoardTitle = boardRepository.findDetailByBoardUuid(boardUuid)
                .orElseThrow(RuntimeException::new).getBoardTitle();

        // then
        assertThat(getBoardTitle).isEqualTo(boardTitle);
    }

    @Test
    @DisplayName("업데이트, 삭제용 엔티티 불러오기")
    void GetEntity(){
        // given
            // 사전 데이터
        String memberUuid = "uuidUpdateDelete";
        RequestCreateBoardDto createBoardDto = new RequestCreateBoardDto(
                BoardType.AUCTION,
                "Auction Content",
                "Auction Title",
                "Auction Product",
                10,
                100,
                "Auction Source",
                "Auction Image"
        );
            // 저장
        Board board = createBoardDto.toBoard(memberUuid, "memberEntity");
        boardRepository.save(board);
        String boardUuid = board.getBoardUuid();

        // when
        String getMemberUuid = boardRepository.findByBoardUuidAndBoardStatusTrue(boardUuid)
                .orElseThrow(RuntimeException::new).getMemberUuid();

        // then
        assertThat(getMemberUuid).isEqualTo(memberUuid);
    }
}