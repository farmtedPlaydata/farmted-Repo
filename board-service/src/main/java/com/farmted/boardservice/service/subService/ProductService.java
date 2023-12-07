package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.dto.request.RequestUpdateProductBoardDto;
import com.farmted.boardservice.dto.response.detailDomain.ResponseGetProductDetailDto;
import com.farmted.boardservice.dto.response.listDomain.ResponseGetProductDto;
import com.farmted.boardservice.enums.BoardType;
import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.FeignDomainType;
import com.farmted.boardservice.feignClient.ProductFeignClient;
import com.farmted.boardservice.util.feignConverter.FeignConverter;
import com.farmted.boardservice.vo.ProductVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
// Board-Service 내의 상품에 대한 로직을 담당
public class ProductService {
    private final ProductFeignClient productFeignClient;
    private final FeignConverter<ProductVo> productConverter;

    // 상품 저장 및 예외처리
    public void postProduct(ProductVo productVo, String uuid){
        productConverter.convertSingleVo(
                productFeignClient.createProductData(productVo, uuid),
                FeignDomainType.PRODUCT,
                ExceptionType.SAVE
        );
    }
// 카테고리별 게시글 리스트
    // 전체 게시글 리스트
    public List<ResponseGetProductDto> getProductList(BoardType category, int pageNo){
        return productConverter.convertListVo(productFeignClient.getProductList(category, pageNo),
                        FeignDomainType.PRODUCT, ExceptionType.GETLIST)
                .stream().map(ResponseGetProductDto::new)
                .toList();
    }

    // 특정 유저가 작성한 게시글 리스트 조회
    public List<ResponseGetProductDto> getProductListByMember(String uuid, BoardType category, int pageNo){
        return productConverter.convertListVo(productFeignClient.getProductListSeller(uuid, category, pageNo),
                        FeignDomainType.PRODUCT,
                        ExceptionType.GETLIST)
                .stream().map(ResponseGetProductDto::new)
                .toList();
    }

    // 개별 상품 Detail 값
    public ResponseGetProductDetailDto getProductByBoardUuid(String boardUuid){
        return new ResponseGetProductDetailDto(
                productConverter.convertSingleVo(
                    productFeignClient.getProductDetail(boardUuid),
                    FeignDomainType.PRODUCT, ExceptionType.GET));
    }

    // 상품 업데이트 요청 (예외처리만 확인)
    public void checkUpdateProduct(String boardUuid, RequestUpdateProductBoardDto updateDTO, String memberUuid){
        productConverter.convertSingleVo(
                productFeignClient.updateProductData(boardUuid, updateDTO.toProduct(boardUuid), memberUuid),
                FeignDomainType.PRODUCT, ExceptionType.UPDATE
        );
    }

    // 상품 삭제 요청 (예외처리만 확인)
    public void checkDeleteProduct(String boardUuid, String memberUuid){
        productConverter.convertSingleVo(
                productFeignClient.deactiveProductStatus(boardUuid, memberUuid),
                FeignDomainType.PRODUCT, ExceptionType.DELETE
        );
    }
}
