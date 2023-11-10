package com.farmted.boardservice.controller;

import com.farmted.boardservice.dto.request.RequestCreateProductBoardDto;
import com.farmted.boardservice.service.BoardService;
import com.farmted.boardservice.vo.ProductVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service")
public class BoardController {
    private final BoardService boardService;

    // 경매 게시글 작성
    @PostMapping(value = "/boards/auctions")
    public ResponseEntity<?> createAuctionBoard(
            @Valid @RequestBody RequestCreateProductBoardDto productBoardDto,
            @RequestHeader("UUID") String uuid,
            @RequestHeader("ROLE") String role){
        ProductVo productVo = boardService.createActionBoard(productBoardDto, uuid, role);
        return new ResponseEntity<>(productVo, HttpStatus.CREATED);
    }

    // 전체 경매 게시글 조회
    @GetMapping(value = "/boards/auctions")
    public ResponseEntity<?> getAuctionBoardList(){
        return new ResponseEntity<>(boardService.getAuctionBoardList(), HttpStatus.OK);
    }

    // 개별 경매 게시글 상세 조회
    @GetMapping(value = "/boards/{board_uuid}/auctions")
    public ResponseEntity<?> getAuctionBoard(@PathVariable(value = "board_uuid") String boardUuid){
        return new ResponseEntity<>(boardService.getAuctionBoard(boardUuid), HttpStatus.OK);
    }
}
