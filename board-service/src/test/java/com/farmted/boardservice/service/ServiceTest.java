package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.RequestCreateProductBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.vo.ProductVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional(readOnly = true)
public class ServiceTest {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @Autowired
    public ServiceTest(BoardService boardService, BoardRepository boardRepository) {
        this.boardService = boardService;
        this.boardRepository = boardRepository;
    }

    @BeforeEach
    public void reset(){
        boardRepository.deleteAll();
    }

    @Test
    @Transactional
    public void createBoardTest(){
        // given
            // 더미데이터 생성
        RequestCreateProductBoardDto dummyData = new RequestCreateProductBoardDto(
                BoardType.AUCTION,             // BoardType 값
                "게시글 내용",                  // 게시글 내용
                "게시글 제목",                  // 게시글 제목
                "상품 이름",                    // 상품 이름
                10,                             // 상품 재고
                10_000L,                         // 상품 가격
                "상품 출처",                    // 상품 출처
                "상품 이미지 URL"               // 상품 이미지 URL
        );

        // when
            // 통신을 배제해서 게시글 데이터는 저장하고
            // 상품 데이터는 컨트롤러로 반환
         ProductVo productVo = boardService.createBoard(dummyData);

        // then
        Board checkBoard = boardRepository.findByBoardTitle("게시글 제목").get();
            // 1. 저장한 Board와 더미데이터의 데이터 값이 같은가
        assertThat(dummyData.boardContent())
                .isEqualTo(checkBoard.getBoardContent());
            // 2. 전송할 ProductVO가 입력한 값과 같은가
        assertThat(productVo.toString()).isEqualTo(
                dummyData.toProduct().toString()
                );
    }
}
