package com.smartlearning.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Course Service安全配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF保护
                .csrf(AbstractHttpConfigurer::disable)
                
                // 禁用CORS（由网关处理）
                .cors(AbstractHttpConfigurer::disable)
                
                // 配置会话管理为无状态
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // 配置请求授权
                .authorizeHttpRequests(auth -> auth
                        // 允许访问的公开接口
                        .requestMatchers(
                                "/api/course/public/**",       // 公开课程API
                                "/actuator/**",                // 监控端点
                                "/swagger-ui/**",              // Swagger文档
                                "/v3/api-docs/**",             // API文档
                                "/swagger-resources/**",       // Swagger资源
                                "/webjars/**"                  // Web资源
                        )
                        .permitAll()
                        
                        // 其他所有请求都需要认证
                        .anyRequest().authenticated())
                
                // 禁用默认登录页面
                .formLogin(AbstractHttpConfigurer::disable)
                
                // 禁用HTTP Basic认证
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}