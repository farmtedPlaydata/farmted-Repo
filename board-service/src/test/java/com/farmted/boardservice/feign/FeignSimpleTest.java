//package com.farmted.boardservice.feign;
//import com.farmted.boardservice.feignClient.ProductFeignClient;
//import com.github.tomakehurst.wiremock.WireMockServer;
//import com.github.tomakehurst.wiremock.client.WireMock;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@DisplayName("Feign 통신 테스트 코드 단순 예제")
//public class FeignSimpleTest {
//    private static WireMockServer wireMockServer;
//
//    private final ProductFeignClient productFeignClient;
//
//    public FeignSimpleTest(ProductFeignClient productFeignClient) {
//        this.productFeignClient = productFeignClient;
//    }
//
//    @BeforeAll
//    public static void setup() {
//        wireMockServer = new WireMockServer(); // 8080 포트로 기본 설정
//        wireMockServer.start();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        wireMockServer.stop();
//    }
//
//    @Test
//    public void testWireMock() {
//        // WireMock 서버에 HTTP 요청 및 응답 설정
//        stubFor(get(urlEqualTo("/example"))
//                .willReturn(aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", "text/plain")
//                        .withBody("Hello, WireMock!")));
//
//        // WireMock 서버가 예상대로 응답하는지 확인
//        WireMock.verify(getRequestedFor(urlEqualTo("/example")));
//
//        productFeignClient.getProductDetail("1234");
//
//        assertThat();
//    }
//}
