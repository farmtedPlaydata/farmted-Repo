package com.farmted.boardservice.controller;

import com.farmted.boardservice.dto.request.RequestCreateBoardDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.RoleEnums;
import com.farmted.boardservice.facade.BoardFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardController.class)
@MockBean(JpaMetamodelMappingContext.class)
// @EnableJpaAuditing때문에 빈등록이 안되어 Mock 설정
class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardFacade boardFacade;

    @Test
    void createBoard() throws Exception{
        // given
        doNothing().when(boardFacade).createBoard(any(RequestCreateBoardDto.class), anyString(), any(RoleEnums.class), any());
        RequestCreateBoardDto requestDto = new RequestCreateBoardDto(
                BoardType.AUCTION,             // BoardType 값
                "게시글 내용",                  // 게시글 내용
                "게시글 제목",                  // 게시글 제목
                "상품 이름",                    // 상품 이름
                10,                             // 상품 재고
                10_000L,                         // 상품 가격
                "상품 출처"                   // 상품 출처
        );
        MockMultipartFile image = new MockMultipartFile("image", "sample.jpg", MediaType.IMAGE_JPEG_VALUE, "Image Content".getBytes());

        // when
        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/board-service/boards")
                        .file(new MockMultipartFile("CREATE", "", "application/json", objectMapper.writeValueAsString(requestDto).getBytes()))
                        .file("IMAGE", image.getBytes())
                        .header("UUID", "sampleUUID")
                        .header("ROLE", "ROLE_USER"))
        // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSuccess").value(true));
    }

    @Test
    void getBoardList() {
    }

    @Test
    void getAuctionBoard() {
    }

    @Test
    void getBoardListWriter() {
    }

    @Test
    void updateAuctionBoard() {
    }

    @Test
    void deleteAuctionBoard() {
    }
}