package com.farmted.boardservice.enums;

// Feign 통신 시도 중인 도메인
public enum FeignDomainType {
    PRODUCT("Product-Service"),
    AUCTION("Auction-Service"),
    MEMBER("Member-Serivce");

    private final String name;

    FeignDomainType(String name) {this.name = name;}

    @Override
    public String toString() {
        return super.toString();
    }
}
