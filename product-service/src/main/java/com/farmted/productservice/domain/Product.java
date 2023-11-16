package com.farmted.productservice.domain;

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
    private int price;

    @NotNull
    private String source;

    @Column(nullable = true)
    private String image;

    @NotNull
    private boolean status;

    @NotNull
    private boolean auctionStatus;

    @NotNull
    private String memberUuid;

    @NotNull
    private String boardUuid;

    @PrePersist
    public void createUuid(){
        uuid = UUID.randomUUID().toString();
        status = true;
        auctionStatus = true;
    }

    // 가격 수정
    public void modifyPrice(int price){
        this.price =price;
    }


}
