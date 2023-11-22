package com.farmted.gatewayservice.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            // 헤더에 uuid, role값 강제로 넣기
            request.mutate().header("UUID", request.getHeaders().get("UUID").get(0))
                            .header("ROLE", request.getHeaders().get("ROLE").get(0))
                    .build();
            // pre filter는 그냥 내부에 실행문으로 작성하면 돌아갑니다.
            System.out.println("Custom pre filter : " + request.getId());
            System.out.println("Custom pre path : " + request.getPath());
            // post filter는 return구문 속에 코드를 작성해서 만듭니다.
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                System.out.println("Custom post filter : " + response.getStatusCode());
            }));
        };
    }

    public static class Config {

    }
}
