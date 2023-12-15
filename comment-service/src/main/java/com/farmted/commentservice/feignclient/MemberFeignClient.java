package com.farmted.commentservice.feignclient;


import com.farmted.commentservice.util.GlobalResponseDto;
import com.farmted.commentservice.vo.MemberVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name= "member-service", path = "/member-service")
public interface MemberFeignClient {
    @GetMapping("/member/info")
    public ResponseEntity<GlobalResponseDto<MemberVo>>memberNameAndImage(@RequestHeader("UUID") String memberUuid);
}
