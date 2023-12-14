package com.farmted.boardservice.service.subService;

import com.farmted.boardservice.enums.ExceptionType;
import com.farmted.boardservice.enums.FeignDomainType;
import com.farmted.boardservice.feignClient.MemberFeignClient;
import com.farmted.boardservice.util.feignConverter.FeignConverter;
import com.farmted.boardservice.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberFeignClient memberFeignClient;
    private final FeignConverter<MemberVo> memberConverter;

    // 글 작성 시 회원 정보 받기
    public MemberVo getMemberInfo(String memberUuid){
        return memberConverter.convertSingleVo(
                memberFeignClient.getMemberInfo(memberUuid),
                FeignDomainType.MEMBER, ExceptionType.GET);
    }
}