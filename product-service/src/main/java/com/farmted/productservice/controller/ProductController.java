package com.farmted.productservice.controller;

import com.farmted.productservice.Facade.ProductTypeFacade;
import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.request.ProductUpdateRequestDto;
import com.farmted.productservice.dto.response.ProductAuctionResponseDto;
import com.farmted.productservice.dto.response.ProductResponseDto;
import com.farmted.productservice.enums.ProductType;
import com.farmted.productservice.service.ProductService;
import com.farmted.productservice.util.GlobalResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "product API", description = "상품 기본 API에 대한 설명 + product -service API")
@RequestMapping("product-service")
public class ProductController {

    private final ProductService productService;
    private final ProductTypeFacade productTypeFactory;

// 판매자 상품 등록
    // TODO: 단순 상품, 경매 상품 구분
    @PostMapping("/products/boards")
    @Operation(summary = "상품 등록", description = "판매,경매로 상품 종류 구분")
    public ResponseEntity<?>  saveProduct(
            @RequestBody ProductSaveRequestDto productSaveRequestDto,
            @RequestHeader("UUID") String uuid // 멤버
    ) {
        productTypeFactory.createProduct(uuid,productSaveRequestDto);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

// 판매자 전체 수정
    @PutMapping("/products/{board_uuid}/boards")
    @Operation(summary = "상품 정보 수정", description = "경매 진행 중이 아닌 상품만 정보 수정 가능")
    public ResponseEntity<?> modifyProduct(
            @PathVariable (value = "board_uuid") String boardUuid ,
            @RequestHeader("UUID") String memberUuid, // 멤버
            @RequestBody ProductUpdateRequestDto productUpdateRequestDto
    )
    {
        productService.modifyProduct(boardUuid, productUpdateRequestDto,memberUuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }


 // 판매자 등록 전체 상품 조회 BoardType
    @GetMapping("/products/seller/{member_uuid}")
    @Operation(summary = "판매자가 등록한 상품 조회", description = "판매,경매로 상품 종류 구분")
    public ResponseEntity<?> getProductListSeller(
            @PathVariable (value = "member_uuid") String memberUuid,
            @RequestParam ("category") ProductType productType,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ) {
        List<ProductAuctionResponseDto> listProductSeller = productTypeFactory.getListMember(productType,memberUuid,pageNo);
        return  ResponseEntity.ok(GlobalResponseDto.listOf(listProductSeller));
    }


// 전체 상품 조회 BoardType
    @GetMapping("/products")
    @Operation(summary = "상품 목록 조회", description = "판매,경매로 상품 종류 구분")
    public ResponseEntity<?> getProductList(
            @RequestParam ("category") ProductType productType,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ){

        List<ProductAuctionResponseDto> listProduct = productTypeFactory.getList(productType, pageNo);
        return ResponseEntity.ok(GlobalResponseDto.listOf(listProduct));
    }


// 상품 상세 조회
    @GetMapping("/products/{board_uuid}/boards")
    @Operation(summary = "상품 세부 내역 조회")
    public ResponseEntity<?> getProductDetail(@PathVariable (value = "board_uuid") String boardUuid){
        ProductResponseDto productDetail = productService.getProductDetail(boardUuid);
        return ResponseEntity.ok(GlobalResponseDto.of(productDetail));

    }

// 상품 삭제
    @DeleteMapping("/products/{board_uuid}/board")
    @Operation(summary = "상품 삭제", description = "판매 상품만 삭제 가능, 경매 진행 중인 상품은 삭제 불가능, 경매 삭제는 관리자만 가능")
    public ResponseEntity<?> deleteProduct(@PathVariable (value = "board_uuid") String boardUuid){
        productService.deleteProduct(boardUuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

}


