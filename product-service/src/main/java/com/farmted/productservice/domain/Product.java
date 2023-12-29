package com.farmted.productservice.domain;


import com.farmted.productservice.dto.request.ProductUpdateRequestDto;
import com.farmted.productservice.enums.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity @Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Product extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String uuid; // 식별자

    @NotNull
    private String name;

    private int stock;

    @NotNull
    private Integer price;

    @NotNull
    private String source;

    @Column(nullable = true)
    private String image;

    @NotNull
    private boolean status;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private boolean auctionStatus;

    @NotNull
    private String memberUuid;

    @NotNull
    private String boardUuid;

    @PrePersist
    public void createUuid(){
        uuid = UUID.randomUUID().toString();
        status = true;
        auctionStatus = true; // 1이 경매중=> true, 0이 경매 완료=> false
    }


    public void modifyProduct(ProductUpdateRequestDto productUpdateRequestDto){
        this.name = productUpdateRequestDto.name();
        this.stock= productUpdateRequestDto.stock();
        this.price= productUpdateRequestDto.price();
        this.source= productUpdateRequestDto.source();
        this.image = productUpdateRequestDto.image();
    }

    public void closedStatus(){
        this.auctionStatus=false;
    }


}

