package com.farmted.productservice.controller;

import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.request.ProductUpdateRequestDto;
import com.farmted.productservice.dto.response.ProductAuctionResponseDto;
import com.farmted.productservice.dto.response.ProductResponseDto;
import com.farmted.productservice.Facade.ProductTypeFacade;
import com.farmted.productservice.enums.ProductType;
import com.farmted.productservice.service.AuctionService;
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
    private final AuctionService auctionService;
    private final ProductTypeFacade productTypeFactory;

// 판매자 상품 등록
    // TODO: 단순 상품, 경매 상품 구분
    @PostMapping("/products/boards")
    public ResponseEntity<?>  saveProduct(
            @RequestBody ProductSaveRequestDto productSaveRequestDto,
            @RequestHeader("UUID") String uuid // 멤버
    ) {
        productTypeFactory.createProduct(uuid,productSaveRequestDto);
        //productService.saveProduct(uuid,productSaveRequestDto);
        return ResponseEntity.ok(GlobalResponseDto.of(true));
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


 // 판매자 등록 전체 상품 조회 BoardType
    @GetMapping("/products/seller/{member_uuid}")
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
    public ResponseEntity<?> getProductList(
            @RequestParam ("category") ProductType productType,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo
    ){

        List<ProductAuctionResponseDto> listProduct = productTypeFactory.getList(productType, pageNo);
        return ResponseEntity.ok(GlobalResponseDto.listOf(listProduct));
    }


// 상품 상세 조회
    @GetMapping("/products/{board_uuid}/boards")
    public ResponseEntity<?> getProductDetail(@PathVariable (value = "board_uuid") String boardUuid){
        ProductResponseDto productDetail = productService.getProductDetail(boardUuid);
        return ResponseEntity.ok(GlobalResponseDto.of(productDetail));

    }

// 상품 삭제
//    @DeleteMapping("/products/{board_uuid}/board")
//    public ResponseEntity<?> deleteProduct(@PathVariable (value = "board_uuid") String boardUuid){
//
//    }

}


