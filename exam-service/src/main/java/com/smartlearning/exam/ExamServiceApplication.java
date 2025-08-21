package com.smartlearning.exam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 考试服务启动类
 */
@SpringBootApplication(scanBasePackages = {"com.smartlearning.exam", "com.smartlearning.common"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.smartlearning.exam.mapper")
public class ExamServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExamServiceApplication.class, args);
    }
}
