package com.farmted.boardservice.feignClient;

import com.farmted.boardservice.util.GlobalResponseDto;
import com.farmted.boardservice.vo.MemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${service.member.name}", path= "${service.member.url}")
public interface MemberFeignClient {
    @GetMapping("/member/info")
    ResponseEntity<GlobalResponseDto<MemberVo>> getMemberInfo(@RequestHeader("UUID") String memberUuid);
}
