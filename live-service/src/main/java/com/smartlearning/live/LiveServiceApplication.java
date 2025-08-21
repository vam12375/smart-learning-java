package com.smartlearning.live;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 直播服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.smartlearning.live", "com.smartlearning.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.smartlearning.live.mapper")
public class LiveServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(LiveServiceApplication.class, args);
    }
}
