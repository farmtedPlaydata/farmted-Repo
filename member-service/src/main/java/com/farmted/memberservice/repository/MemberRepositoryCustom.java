package com.farmted.memberservice.repository;

import com.farmted.memberservice.dto.request.SearchMemberParam;
import com.farmted.memberservice.dto.response.MemberResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<MemberResponseDto> findAll(SearchMemberParam dto, Pageable pageable);
}
