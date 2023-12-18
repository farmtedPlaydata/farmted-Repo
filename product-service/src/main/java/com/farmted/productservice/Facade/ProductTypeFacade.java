package com.farmted.productservice.Facade;


import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.response.ProductAuctionResponseDto;
import com.farmted.productservice.enums.ProductType;
import com.farmted.productservice.service.AuctionService;
import com.farmted.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductTypeFacade {

    private final ProductService productService;
    private final AuctionService auctionService;

    // ProductType에 따라 적절한 생성 메서드 호출
    public void createProduct(String memberUuid, ProductSaveRequestDto productSaveRequestDto) {
        String saved = productService.saveProduct(memberUuid, productSaveRequestDto);

        switch (productSaveRequestDto.getProductType()){
            case SALE: //판매
                break;

            case AUCTION: // 경매
                // Product일때 추가로 동작
                auctionService.createProductToAuction(saved);
                break;
        }

    }

    // 목록 조회
    // productType에 따라 적절한 List조회 구현 메서드를 선택
    public List<ProductAuctionResponseDto> getList(String productType, int pageNo){

        switch (productType){
            case "SALE":
                return productService.getListProduct(pageNo);
            case "PRODUCT":
                return auctionService.getListProductAuction(pageNo);
        }

        return null;
    }

    // 판매자 등록 목록 조회
}
