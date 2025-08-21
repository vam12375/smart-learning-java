package com.smartlearning.analytics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 数据分析服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.smartlearning.analytics", "com.smartlearning.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.smartlearning.analytics.mapper")
public class AnalyticsServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AnalyticsServiceApplication.class, args);
    }
}
