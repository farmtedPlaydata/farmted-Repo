package com.farmted.boardservice.service.subService;

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

    // 특정 유저가 작성한 게시글 리스트 조회
    public List<ResponseGetProductDto> productList(String uuid, BoardType boardType, int pageNo){
        return productConverter.convertListVo(productFeignClient.getProductListSeller(uuid, boardType, pageNo),
                        FeignDomainType.PRODUCT,
                        ExceptionType.GETLIST)
                .stream()
                .map(ResponseGetProductDto::new)
                .toList();
    }
}
