package com.farmted.boardservice.controller;

import com.farmted.boardservice.dto.RequestCreateProductBoardDto;
import com.farmted.boardservice.service.BoardService;
import com.farmted.boardservice.vo.ProductVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service")
public class BoardController {
    private final BoardService boardService;

    @PostMapping(value = "/boards")
    public ResponseEntity<?> createRecord(@Valid @RequestBody RequestCreateProductBoardDto productBoardDto){
        ProductVo productVo = boardService.createBoard(productBoardDto);
        return ResponseEntity.ok(productVo);
    }
}
