package com.farmted.boardservice.controller;

import com.farmted.boardservice.dto.request.RequestCreateProductBoardDto;
import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board API", description = "게시글에 대한 전반적인 요청을 수용하는 Board-Service API")
@RequestMapping("/board-service")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/boards/auctions")
    @Operation(summary = "경매 게시글 작성", description = "카테고리가 경매인 게시글의 등록 요청")
    public ResponseEntity<?> createAuctionBoard(
            @Valid @RequestBody RequestCreateProductBoardDto productBoardDto,
            @RequestHeader("UUID") String uuid,
            @RequestHeader("ROLE") String role){
        boardService.createActionBoard(productBoardDto, uuid, role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/boards/auctions")
    @Operation(summary = "N 페이지의 경매 게시글 조회", description = "N 페이지에 대한 경매 게시글 조회 요청")
    public ResponseEntity<?> getAuctionBoardList(@RequestParam(required = false,
                                                         defaultValue = "0",
                                                                value = "page") int pageNo){
        return new ResponseEntity<>(boardService.getAuctionBoardList(pageNo-1), HttpStatus.OK);
    }

    @GetMapping(value = "/boards/{board_uuid}/auctions")
    @Operation(summary = "경매 게시글 상세 조회", description = "개별 경매 게시글에 대한 단일 조회 요청")
    public ResponseEntity<?> getAuctionBoard(@PathVariable(value = "board_uuid") String boardUuid){
        return new ResponseEntity<>(boardService.getAuctionBoard(boardUuid), HttpStatus.OK);
    }

    // 개별 경매 게시글 수정
    @PutMapping(value = "/boards/{board_uuid}/auctions")
    @Operation(summary = "개별 경매 게시글 수정", description = "개별 경매 게시글에 대한 수정 요청")
    public ResponseEntity<?> updateAuctionBoard(@PathVariable(value= "board_uuid") String boardUuid,
                                                @Valid @RequestBody RequestUpdateProductBoardDto updateDTO,
                                                @RequestHeader("UUID") String uuid){
        boardService.updateAuctionBoard(updateDTO, boardUuid, uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        // Feign 통신 구현 후엔 아래 값 리턴 예정 (성공적으로 요청을 처리했지만, 응답할 내용 없음->비동기처리할거니까)
    }

    // 개별 경매 게시글 삭제
    @DeleteMapping(value = "/boards/{board_uuid}/auctions")
    @Operation(summary = "개별 경매 게시글 삭제", description = "개별 경매 게시글에 대한 삭제 요청")
    public ResponseEntity<?> deleteAuctionBoard(@PathVariable(value = "board_uuid") String boardUuid,
                                                @RequestHeader("UUID") String uuid) {
        boardService.deleteAuctionBoard(boardUuid, uuid);
        // 헤더 설정이므로 컨트롤러에서 사용했음. 클라이언트와의 상호작용이므로
            // 상태코드 303, 리다이렉트를 나타냄 + 어차피 조회 메소드로 이동할거니까 body값 null
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create("/board-service/boards/auctions"))
                .body(null);
    }
}
