package com.farmted.boardservice.service;

import com.farmted.boardservice.domain.Board;
import com.farmted.boardservice.dto.RequestCreateProductBoardDto;
import com.farmted.boardservice.repository.BoardRepository;
import com.farmted.boardservice.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public ProductVo createBoard(RequestCreateProductBoardDto boardDto){
        // 게시글 Entity 생성 - 저장
        Board board = boardDto.toBoard("임의UUID");
        boardRepository.save(board);

        // 상품 VO 생성 - 전송
        ProductVo productVo = boardDto.toProduct();
        return productVo;
    }
}
