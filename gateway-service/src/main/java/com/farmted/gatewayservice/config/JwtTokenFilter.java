package com.farmted.gatewayservice.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class JwtTokenFilter extends AbstractGatewayFilterFactory<JwtTokenFilter.Config> {

    private final JwtProvider jwtProvider;

    public JwtTokenFilter(JwtProvider jwtProvider) {
        super(Config.class);
        this.jwtProvider = jwtProvider;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(JwtTokenFilter.Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String token = exchange.getRequest().getCookies().getFirst("Authorization").getValue().substring(7);

            if (!jwtProvider.validateToken(token)) {
                return onError(exchange, "Jwt Token is not valid", HttpStatus.UNAUTHORIZED);
                // pass-service로 재전송 *Front 에서 처리
            } else {
                // 헤더에 UUID, role 집어넣기
                Claims memberInfo = jwtProvider.getUserInfoFromToken(token);
                String uuid = memberInfo.getSubject();
                String role = memberInfo.get(JwtProvider.AUTH_KEY).toString();
                log.info("uuid : " + uuid);
                log.info("role : " + role);
                request.mutate().header("UUID", uuid)
                        .header("ROLE", role)
                        .build();
            }

            return chain.filter(exchange);

        };
    }

    // 단일값: mono, 다중값: flux
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }
}
