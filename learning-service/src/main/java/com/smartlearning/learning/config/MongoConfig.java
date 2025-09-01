package com.smartlearning.learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

/**
 * MongoDB配置类
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.smartlearning.learning.repository")
public class MongoConfig extends AbstractMongoClientConfiguration {
    
    @Override
    protected String getDatabaseName() {
        return "smart_learning";
    }
    
    /**
     * 自定义转换器，处理LocalDateTime和LocalDate
     */
    @Bean
    @Override
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
                // LocalDateTime 转换器
                new org.springframework.core.convert.converter.Converter<LocalDateTime, Date>() {
                    @Override
                    public Date convert(LocalDateTime source) {
                        return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
                    }
                },
                new org.springframework.core.convert.converter.Converter<Date, LocalDateTime>() {
                    @Override
                    public LocalDateTime convert(Date source) {
                        return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    }
                },
                // LocalDate 转换器
                new org.springframework.core.convert.converter.Converter<LocalDate, Date>() {
                    @Override
                    public Date convert(LocalDate source) {
                        return Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    }
                },
                new org.springframework.core.convert.converter.Converter<Date, LocalDate>() {
                    @Override
                    public LocalDate convert(Date source) {
                        return source.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    }
                }
        ));
    }
}
