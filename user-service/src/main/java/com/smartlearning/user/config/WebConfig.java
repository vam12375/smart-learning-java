package com.smartlearning.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Web配置类
 * 配置静态资源访问
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    /**
     * 初始化上传目录
     */
    @PostConstruct
    public void initUploadDirectory() {
        try {
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                System.out.println("创建上传目录: " + uploadDir.toAbsolutePath());
            }

            // 创建头像子目录
            Path avatarDir = uploadDir.resolve("avatars");
            if (!Files.exists(avatarDir)) {
                Files.createDirectories(avatarDir);
                System.out.println("创建头像目录: " + avatarDir.toAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("创建上传目录失败: " + e.getMessage());
        }
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 获取绝对路径
        String absolutePath = Paths.get(uploadPath).toAbsolutePath().toString();

        System.out.println("配置静态资源访问路径: " + absolutePath);

        // 配置上传文件的静态资源访问
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolutePath + File.separator)
                .setCachePeriod(3600); // 缓存1小时
    }
}
