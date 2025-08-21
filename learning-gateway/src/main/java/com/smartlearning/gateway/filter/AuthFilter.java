package com.smartlearning.gateway.filter;

import com.smartlearning.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * JWT认证过滤器
 */
@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 不需要认证的路径
     */
    private static final List<String> SKIP_AUTH_PATHS = Arrays.asList(
        "/auth/login",
        "/auth/register",
        "/auth/refresh",
        "/course/public",
        "/actuator",
        "/doc.html",
        "/swagger-ui",
        "/v3/api-docs"
    );
    
    public AuthFilter() {
        super(Config.class);
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            
            // 检查是否需要跳过认证
            if (shouldSkipAuth(path)) {
                return chain.filter(exchange);
            }
            
            // 获取Authorization头
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
                return handleUnauthorized(exchange, "缺少认证信息");
            }
            
            // 提取Token
            String token = authHeader.substring(7);
            
            // 验证Token
            if (!jwtUtil.validateToken(token)) {
                return handleUnauthorized(exchange, "Token无效或已过期");
            }
            
            // 从Token中提取用户信息并添加到请求头
            try {
                Long userId = jwtUtil.getUserIdFromToken(token);
                String username = jwtUtil.getUsernameFromToken(token);
                String role = jwtUtil.getRoleFromToken(token);
                
                if (userId != null && StringUtils.hasText(username)) {
                    ServerHttpRequest mutatedRequest = request.mutate()
                            .header("X-User-Id", String.valueOf(userId))
                            .header("X-Username", username)
                            .header("X-User-Role", role)
                            .build();
                    
                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                } else {
                    return handleUnauthorized(exchange, "Token中用户信息无效");
                }
            } catch (Exception e) {
                log.error("Token解析失败", e);
                return handleUnauthorized(exchange, "Token解析失败");
            }
        };
    }
    
    /**
     * 检查是否应该跳过认证
     */
    private boolean shouldSkipAuth(String path) {
        return SKIP_AUTH_PATHS.stream().anyMatch(path::startsWith);
    }
    
    /**
     * 处理未认证请求
     */
    private Mono<Void> handleUnauthorized(org.springframework.web.server.ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        
        String body = String.format(
            "{\"code\":401,\"message\":\"%s\",\"timestamp\":\"%s\"}",
            message,
            System.currentTimeMillis()
        );
        
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
    
    public static class Config {
        // 配置类，可以添加配置参数
    }
}
