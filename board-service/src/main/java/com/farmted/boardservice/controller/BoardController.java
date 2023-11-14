package com.farmted.boardservice.controller;

import com.farmted.boardservice.dto.request.RequestCreateProductBoardDTO;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDTO;
import com.farmted.boardservice.service.BoardService;
import com.farmted.boardservice.vo.ProductVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board-service")
public class BoardController {
    private final BoardService boardService;

    // 경매 게시글 작성
    @PostMapping(value = "/boards/auctions")
    public ResponseEntity<?> createAuctionBoard(
            @Valid @RequestBody RequestCreateProductBoardDTO productBoardDto,
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

    // 개별 경매 게시글 삭제
    @DeleteMapping(value = "/boards/{board_uuid}/auctions")
    public ResponseEntity<?> deleteAuctionBoard(@PathVariable(value = "board_uuid") String boardUuid) {
        boardService.deleteAuctionBoard(boardUuid);
        // 헤더 설정이므로 컨트롤러에서 사용했음. 클라이언트와의 상호작용이므로
            // 상태코드 303, 리다이렉트를 나타냄 + 어차피 조회 메소드로 이동할거니까 body값 null
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create("/board-service/boards/auctions"))
                .body(null);
    }

    // 개별 경매 게시글 수정
    @PutMapping(value = "/boards/{board_uuid}/auctions")
    public ResponseEntity<?> updateAuctionBoard(@PathVariable(value= "board_uuid") String boardUuid,
                                        @Valid @RequestBody RequestUpdateProductBoardDTO updateDTO){
        ProductVo productVo = boardService.updateAuctionBoard(updateDTO, boardUuid);
        return new ResponseEntity<>(productVo, HttpStatus.OK);
        // Feign 통신 구현 후엔 아래 값 리턴 예정 (성공적으로 요청을 처리했지만, 응답할 내용 없음->비동기처리할거니까)
        // return  ResponseEntity.status(HttpStatus.NO_CONTENT);
    }
}
