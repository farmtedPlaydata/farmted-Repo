package com.farmted.memberservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "pass-service", path = "/pass-service")
public interface PassFeignClient {


}
