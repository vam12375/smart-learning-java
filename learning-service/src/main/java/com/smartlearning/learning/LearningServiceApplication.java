package com.smartlearning.learning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 学习服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.smartlearning.learning", "com.smartlearning.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.smartlearning.learning.mapper")
public class LearningServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LearningServiceApplication.class, args);
    }
}
