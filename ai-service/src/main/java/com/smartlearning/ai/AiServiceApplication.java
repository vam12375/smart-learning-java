package com.smartlearning.ai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * AI服务启动类
 */
@SpringBootApplication(scanBasePackages = { "com.smartlearning.ai", "com.smartlearning.common" })
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
@EnableScheduling
@MapperScan("com.smartlearning.ai.mapper")
public class AiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiServiceApplication.class, args);
    }
}
