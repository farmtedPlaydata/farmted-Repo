package com.farmted.gatewayservice.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.security.Key;
import java.util.Base64;

@Component
@Slf4j
public class JwtTokenFilter extends AbstractGatewayFilterFactory<JwtTokenFilter.Config> {

    @Value("${jwt-secret-key}")
    private String secretKey;
    private Key key;

    public JwtTokenFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 쿠키에서 토큰 추출
            String token = extractTokenFromCookie(exchange);
            log.info("JwtTokenFilter - token : " + token);

            // 토큰이 있고 유효한 경우에만 통과
            if (token != null && isValidToken(token)) {
                // 헤더에 토큰을 추가
                exchange.getRequest().mutate().header("Authorization", token);

                return chain.filter(exchange);
            } else {
                // 유효하지 않은 토큰이면 리다이렉트 또는 에러 처리
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    private String extractTokenFromCookie(ServerWebExchange exchange) {
        HttpCookie cookie = exchange.getRequest().getCookies().getFirst("Authorization");

        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    private boolean isValidToken(String token) {
        boolean returnValue = true;
        String subject = null;
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);

        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();

            subject = jwtParser.parseClaimsJws(token).getBody().getSubject();
            log.info("inValidToken - subject : " + subject);


        } catch (Exception ex) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) returnValue = false;

        return returnValue;
    }

    public static class Config {

    }


}
