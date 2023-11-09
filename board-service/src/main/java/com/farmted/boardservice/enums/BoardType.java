package com.farmted.boardservice.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum BoardType {
    AUCTION("Auction", "경매"),
    SALE("Sale", "판매"),
    NOTICE("Notice", "공지사항"),
    CUSTOMER_SERVICE("CustomerService", "고객센터");

    private String typeEn;
    private String typeKo;

    BoardType(String typeEn, String typeKo) {
        this.typeEn = typeEn;
        this.typeKo = typeKo;
    }

    @JsonCreator
    public static BoardType fromLabel(String checkType) {
        for (BoardType type : BoardType.values()) {
            if (type.typeKo.equalsIgnoreCase(checkType)||type.typeEn.equalsIgnoreCase(checkType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("정해지지 않은 타입입니다.: " + checkType);
    }

    @JsonValue
    public String getLabel() {
        return typeKo;
    }
}