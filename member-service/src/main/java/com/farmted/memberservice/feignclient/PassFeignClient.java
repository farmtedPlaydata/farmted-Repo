package com.farmted.memberservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pass-service", path = "/pass-service")
public interface PassFeignClient {

    @GetMapping("/allpass")
    ResponseEntity<?> findAll(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo);

    @GetMapping("/findbyemail/{email}")
    ResponseEntity<?> findByEmail(@PathVariable String email);
}
