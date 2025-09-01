package com.smartlearning.user.controller;

import com.smartlearning.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Paths;

/**
 * 测试控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
@Tag(name = "测试接口", description = "用于测试和调试的接口")
public class TestController {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    /**
     * 测试文件上传路径配置
     */
    @GetMapping("/upload-path")
    @Operation(summary = "测试上传路径", description = "检查文件上传路径配置")
    public Result<String> testUploadPath() {
        String absolutePath = Paths.get(uploadPath).toAbsolutePath().toString();
        File uploadDir = new File(absolutePath);
        
        String info = String.format(
            "上传路径配置: %s\n绝对路径: %s\n目录存在: %s\n可读: %s\n可写: %s",
            uploadPath,
            absolutePath,
            uploadDir.exists(),
            uploadDir.canRead(),
            uploadDir.canWrite()
        );
        
        log.info("上传路径测试: {}", info);
        return Result.success("路径测试完成", info);
    }
}
