package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetProductDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.feignClient.ProductFeignClient;
import com.farmted.boardservice.util.GlobalResponseDto;
import com.farmted.boardservice.util.feignConverter.FeignConverter;
import com.farmted.boardservice.vo.ProductVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Product-Service 테스트 코드")
public class ProductServiceTest {
    private final ProductFeignClient productFeignClient = mock(ProductFeignClient.class);
    private final FeignConverter<ProductVo> productConverter;

    @Autowired
    ProductServiceTest(FeignConverter<ProductVo> productConverter, ProductService productService) {
        this.productConverter = productConverter;
    }

    private ProductService productService;


    // mock으로 받을 객체값
    private static final ProductVo productDetail = ProductVo.builder()
            .productName("Name")
            .productStock(100)
            .productSource("Source")
            .productImage("Image")
            .boardUuid("boardUUID")
            .boardType(BoardType.AUCTION)
            .productPrice(100_000L)
            .build() ;
    private static final List<ProductVo> productList = IntStream.rangeClosed(1, 3).mapToObj(
            i -> ProductVo.builder()
                    .boardUuid("boardUUID"+i)
                    .productImage("Image"+i)
                    .productSource("Source"+i)
                    .productName("Name"+i)
                    .boardType(BoardType.AUCTION)
                    .productPrice(100_000L * i)
                    .build()).toList();
    @BeforeEach
    void setUp(){
        productService = new ProductService(productFeignClient, productConverter);
    }

    @Test
    @DisplayName("상품 저장 요청")
    void postProduct(){
        // given
        String memberUuid = "uuid";
        ProductVo productVo = ProductVo.builder()
                .boardUuid("boardUUID")
                .productImage("Image")
                .productStock(10)
                .productSource("Source")
                .productName("Name")
                .boardType(BoardType.AUCTION)
                .productPrice(100_000L)
                                .build();
        when(productFeignClient.createProductData(productVo, memberUuid))
                .thenReturn(ResponseEntity.ok(GlobalResponseDto.of(null)));

        // when
        productService.postProduct(productVo, memberUuid);

        // then
        verify(productFeignClient, times(1)).createProductData(any(), any());
    }

    @Test
    @DisplayName("카테고리별 전체 게시글 리스트")
    void getProductList(){
        // given
        BoardType category = BoardType.AUCTION;
        int pageNo = 0;
        when(productFeignClient.getProductList(category, pageNo))
                .thenReturn(ResponseEntity.ok(
                        GlobalResponseDto.listOf(productList)));

        // when
        List<ResponseGetProductDto> productDtos = productService.getProductList(category, pageNo);

        // then
        assertThat(productDtos.size()).isEqualTo(productList.size());
    }

    @Test
    @DisplayName("특정 유저가 작성한 게시글 리스트 조회")
    void getProductListByMember(){
        // given
        String memberUuid = "uuid";
        BoardType category = BoardType.AUCTION;
        int pageNo = 0;
        when(productFeignClient.getProductListSeller(memberUuid,category,pageNo))
                .thenReturn(ResponseEntity.ok(
                        GlobalResponseDto.listOf(productList)));
        // when
        List<ResponseGetProductDto> productDtos = productService.getProductListByMember(memberUuid, category, pageNo);
        // then
        assertThat(productDtos.size()).isEqualTo(productList.size());
    }

    @Test
    @DisplayName("개별 상품 Detail 값")
    void getProductByBoardUuid(){
        // given
        String boardUuid = "board-uuid";

        when(productFeignClient.getProductDetail(boardUuid))
                .thenReturn(ResponseEntity.ok(
                        GlobalResponseDto.of(productDetail)));
        // when
        ResponseGetProductDetailDto productDetailDtos = productService.getProductByBoardUuid(boardUuid);
        // then
        assertThat(productDetailDtos.toString())
                .isEqualTo(new ResponseGetProductDetailDto(productDetail).toString());
    }

    @Test
    @DisplayName("상품 업데이트 요청")
    void checkUpdateProduct() {
        // given
        String boardUuid = "board-uuid";
        String memberUuid = "uuid";
        RequestUpdateProductBoardDto updateDTO = new RequestUpdateProductBoardDto(
                BoardType.AUCTION,   // 적절한 값으로 대체
                "Sample Board Content",
                "Sample Board Title",
                "Sample Product Name",
                10,
                10000L,
                "Sample Product Source",
                "Sample Product Image"
        );

        when(productFeignClient.updateProductData(boardUuid, updateDTO.toProduct(boardUuid), memberUuid))
                .thenReturn(ResponseEntity.ok(
                        GlobalResponseDto.of(null)));
        // when
        productService.checkUpdateProduct(boardUuid, updateDTO, memberUuid);
        // then
        verify(productFeignClient, times(1)).updateProductData(boardUuid, updateDTO.toProduct(boardUuid), memberUuid);
    }
}
