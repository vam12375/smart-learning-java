package com.smartlearning.exam.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * API文档配置类
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("智能学习平台 - 考试服务API")
                        .description("考试服务提供题目管理、试卷管理、在线考试等功能的REST API接口")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Smart Learning Team")
                                .email("support@smartlearning.com")
                                .url("https://www.smartlearning.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
