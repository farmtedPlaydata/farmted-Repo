package com.farmted.productservice.Facade;


import com.farmted.productservice.dto.request.ProductSaveRequestDto;
import com.farmted.productservice.dto.response.SaleAuctionTypeResponseDto;
import com.farmted.productservice.dto.response.SaleProductTypeResponseDto;
import com.farmted.productservice.enums.ProductType;
import com.farmted.productservice.exception.ProductException;
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

        switch (productSaveRequestDto.productType()){
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
    public List<SaleProductTypeResponseDto> getList(ProductType productType, int pageNo){

        switch (productType){
            case SALE:
                return productService.getListProduct(pageNo);
            case PRODUCT:
                return auctionService.getListProductAuction(pageNo);
        }
        throw new ProductException(productType);
    }

    // 판매자 등록 목록 조회
    public List<SaleProductTypeResponseDto> getListBuyer(ProductType productType, String memberUuid, int pageNo){
        switch (productType){
            case SALE ->
                productService.getListProductSeller(memberUuid,pageNo);
            case PRODUCT ->
                auctionService.getListMemberProductAuction(memberUuid,pageNo);

        }
        throw new ProductException(productType);
    }

    // 구매 내역 조회
    public List<SaleAuctionTypeResponseDto> getListSeller(ProductType productType, String memberUuid, int pageNo){
        switch (productType){
            case AUCTION ->
                auctionService.getMemberListAuction(memberUuid,pageNo);

        }
        throw new ProductException(productType);
    }

}
