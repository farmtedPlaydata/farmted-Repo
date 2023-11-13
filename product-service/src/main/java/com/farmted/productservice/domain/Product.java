package com.farmted.productservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Product extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid; // FK 사용

    private String name;

    private int stock;

    private int price;

    private String source;

    private String image;

    private boolean status;

    private boolean auctionStatus;

    private String memberUuid;

    private String boardUuid;

    public void modifyPrice(int price){
        this.price =price;
    }


}
