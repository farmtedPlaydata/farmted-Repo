package com.farmted.passservice.dto.querydsl;

import com.farmted.passservice.dto.querydsl.dto.SearchUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SearchPassParam {

    private Boolean status;

    public static SearchPassParam valueOf(SearchUserDto dto) {
        return SearchPassParam.builder()
                .status(false)
                .build();
    }
}
