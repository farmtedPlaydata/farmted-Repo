package com.farmted.gatewayservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class JwtTokenFilter extends AbstractGatewayFilterFactory<JwtTokenFilter.Config> {

    public JwtTokenFilter() {
        super(Config.class);
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(JwtTokenFilter.Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getCookies().getFirst("Authorization").getValue().substring(7);

        }
    }
}
