package com.farmted.productservice.dto.response;

import com.farmted.productservice.domain.Product;
import lombok.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

@Getter @Setter @Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private String name;
    private int stock;
    private int price;
    private String source;
    private String image;
    private boolean status;
    private boolean auctionStatus;


    public ProductResponseDto(Product product) {
        this.name = product.getName();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.source = product.getSource();
        this.image = product.getImage();
        this.status = product.isStatus();
        this.auctionStatus = product.isAuctionStatus();
    }


//    public void setPageableInfo(int pageNumber, int pageSize, Sort sort) {
//        this.pageable = PageRequest.of(pageNumber, pageSize, sort);
//    }
}
