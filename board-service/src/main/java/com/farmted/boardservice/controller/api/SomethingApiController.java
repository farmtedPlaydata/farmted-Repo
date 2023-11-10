package com.farmted.boardservice.controller.api;


import com.farmted.boardservice.vo.ProductVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/something-service")
public class SomethingApiController {
    @PostMapping(value = "/somethings")
    public ResponseEntity<?> getSomethingList(@Valid @PathVariable ProductVo productVo){
        return null;
    }
}
