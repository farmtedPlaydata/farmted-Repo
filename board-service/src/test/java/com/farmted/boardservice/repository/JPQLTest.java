package com.farmted.boardservice.repository;

import com.farmted.boardservice.config.InitDB;
import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.util.JasyptConfig;
import com.farmted.boardservice.vo.MemberVo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.farmted.boardservice.config.InitDB.MEMBER_UUID;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// 해당 어노테이션은 @DataJpaTest에 내장되어 있으며, 이 어노테이션은 '내장된 메모리 데이터베이스'로 테스트를 진행
// 설정을 통해 내장된 메모리 데이터베이스로 변경하지 못하도록 막기
@Import(InitDB.class)
@ActiveProfiles("test")
@DisplayName("JPQL 테스트 코드")
class JPQLTest {
    @Autowired
    private BoardRepository boardRepository;

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
        Page<ResponseGetBoardDto> boardDTO = boardRepository.findByMemberUuidAndBoardType(MEMBER_UUID, BoardType.PRODUCT,
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
        Page<ResponseGetBoardDto> boardDTO = boardRepository.findByMemberUuidAndBoardType(MEMBER_UUID, BoardType.SALE,
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createdAt")));
        // then
        assertThat(boardDTO.getContent().size()).isEqualTo(1);
    }

    @Test
    @Transactional
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
                "Auction Source"
        );
            // 저장
        Board board = createBoardDto.toBoard(memberUuid, new MemberVo("memberDetail", "memberProfile"));
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
    @Transactional
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
                "Auction Source"
        );
            // 저장
        Board board = createBoardDto.toBoard(memberUuid, new MemberVo("memberEntity", "memberProfile"));
        boardRepository.save(board);
        String boardUuid = board.getBoardUuid();

        // when
        String getMemberUuid = boardRepository.findByBoardUuidAndBoardStatusTrue(boardUuid)
                .orElseThrow(RuntimeException::new).getMemberUuid();

        // then
        assertThat(getMemberUuid).isEqualTo(memberUuid);
    }
}