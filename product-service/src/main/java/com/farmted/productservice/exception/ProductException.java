package com.farmted.productservice.exception;

public class ProductException extends RuntimeException{
    public ProductException(){
        super("해당 상품은 존재하지 않습니다.");
    }
    public ProductException(Boolean type){
        super( "경매 상태:"+type+"경매 진행 중에는 수정할 수 없습니다.");
    }
}
