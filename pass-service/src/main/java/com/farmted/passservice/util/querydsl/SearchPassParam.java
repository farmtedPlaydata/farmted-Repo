package com.farmted.passservice.util.querydsl;

import com.farmted.passservice.util.querydsl.dto.SearchUserDto;
import lombok.*;

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
