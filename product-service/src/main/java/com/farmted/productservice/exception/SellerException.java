package com.farmted.productservice.exception;

public class SellerException extends RuntimeException{
    public SellerException(){
        super("해당하는 판매자를 찾을 수 없습니다.");
    }

}
