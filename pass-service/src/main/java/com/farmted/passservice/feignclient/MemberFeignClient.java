package com.farmted.passservice.feignclient;

import com.farmted.passservice.enums.RoleEnums;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "member-service", path = "/member-service")
public interface MemberFeignClient {

    @PostMapping("/member/update-role/{uuid}")
    ResponseEntity<?> updateRole(@PathVariable String uuid);
}
