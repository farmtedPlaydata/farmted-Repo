package com.farmted.productservice.controller;

import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.request.ProductUpdateRequestDto;
import com.farmted.productservice.dto.response.ProductResponseDto;
import com.farmted.productservice.enums.ProductType;
import com.farmted.productservice.service.ProductService;
import com.farmted.productservice.util.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("product-service")
public class ProductController {

    private final ProductService productService;

    // 판매자 상품 등록
    // TODO: 단순 상품, 경매 상품 구분
    @PostMapping("/products/boards")
    public ResponseEntity<?>  saveProduct(
            @RequestBody ProductSaveRequestDto productSaveRequestDto,
            //@RequestParam ProductType productType,
            @RequestHeader("UUID") String uuid // 멤버
    ) {
        productService.saveProduct(uuid,productSaveRequestDto);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    // 판매자 등록 전체 상품 조회
    @GetMapping("/products/seller/{member_uuid}")
    public ResponseEntity<?> getProductListSeller(
            @PathVariable (value = "member_uuid") String memberUuid,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ) {
        List<ProductResponseDto> listProductSeller = productService.getListProductSeller(memberUuid,pageNo);
        return  ResponseEntity.ok(GlobalResponseDto.listOf(listProductSeller));
    }


    // 판매자 전체 수정
    @PutMapping("/products/{board_uuid}/boards")
    public ResponseEntity<?> modifyProduct(
            @PathVariable (value = "board_uuid") String boardUuid ,
            @RequestHeader("UUID") String memberUuid, // 멤버
            @RequestBody ProductUpdateRequestDto productUpdateRequestDto
            )
    {
        productService.modifyProduct(boardUuid, productUpdateRequestDto,memberUuid);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
    }

    // 전체 상품 조회
    @GetMapping("/products")
    public ResponseEntity<?> getProductList(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ){
        List<ProductResponseDto> listProduct = productService.getListProduct(pageNo);
        return ResponseEntity.ok(GlobalResponseDto.listOf(listProduct));
    }

    // 상품 상세 조회
    @GetMapping("/products/{board_uuid}/boards")
    public ResponseEntity<?> getProductDetail(@PathVariable (value = "board_uuid") String boardUuid){
        ProductResponseDto productDetail = productService.getProductDetail(boardUuid);
        return ResponseEntity.ok(GlobalResponseDto.of(productDetail));

    }

}


