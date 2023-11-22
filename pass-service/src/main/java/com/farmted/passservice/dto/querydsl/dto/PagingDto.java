package com.farmted.passservice.dto.querydsl.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter @Setter
public class PagingDto {

    @Min(value = 0)
    private int pageNumber = 0;
    @Range(min = 1, max = 1000)
    private int pageSize = 20;

    public Pageable getPageRequest() {
        return PageRequest.of(pageNumber, pageSize);
    }
}
