package com.smartlearning.user.service.impl;

import com.smartlearning.common.exception.BusinessException;
import com.smartlearning.common.result.ResultCode;
import com.smartlearning.user.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传服务实现类
 */
@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {
    
    @Value("${file.upload.path:/uploads}")
    private String uploadPath;
    
    @Value("${file.upload.domain:http://localhost:8081}")
    private String uploadDomain;
    
    @Value("${file.upload.max-size:5242880}") // 5MB
    private long maxFileSize;
    
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );
    
    private static final int AVATAR_SIZE = 200; // 头像尺寸
    
    @Override
    public String uploadAvatar(MultipartFile file, Long userId) {
        log.info("上传头像请求: userId={}, filename={}", userId, file.getOriginalFilename());
        
        // 验证文件
        if (!validateImageFile(file)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "不支持的图片格式");
        }
        
        try {
            // 生成文件名
            String extension = getFileExtension(file.getOriginalFilename());
            String filename = "avatar_" + userId + "_" + System.currentTimeMillis() + "." + extension;
            
            // 创建目录
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = "avatars/" + dateDir + "/" + filename;
            Path uploadDir = Paths.get(uploadPath, "avatars", dateDir);
            Files.createDirectories(uploadDir);
            
            // 压缩并保存图片
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            BufferedImage resizedImage = resizeImage(originalImage, AVATAR_SIZE, AVATAR_SIZE);
            
            File targetFile = uploadDir.resolve(filename).toFile();
            ImageIO.write(resizedImage, extension, targetFile);
            
            String fileUrl = uploadDomain + "/uploads/" + relativePath;
            log.info("头像上传成功: userId={}, url={}", userId, fileUrl);
            
            return fileUrl;
            
        } catch (IOException e) {
            log.error("头像上传失败: userId={}", userId, e);
            throw new BusinessException(ResultCode.ERROR, "头像上传失败");
        }
    }
    
    @Override
    public String uploadFile(MultipartFile file, String directory) {
        log.info("上传文件请求: filename={}, directory={}", file.getOriginalFilename(), directory);
        
        // 验证文件大小
        if (file.getSize() > maxFileSize) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "文件大小超过限制");
        }
        
        try {
            // 生成文件名
            String extension = getFileExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID().toString() + "." + extension;
            
            // 创建目录
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = directory + "/" + dateDir + "/" + filename;
            Path uploadDir = Paths.get(uploadPath, directory, dateDir);
            Files.createDirectories(uploadDir);
            
            // 保存文件
            Path targetPath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath);
            
            String fileUrl = uploadDomain + "/uploads/" + relativePath;
            log.info("文件上传成功: filename={}, url={}", file.getOriginalFilename(), fileUrl);
            
            return fileUrl;
            
        } catch (IOException e) {
            log.error("文件上传失败: filename={}", file.getOriginalFilename(), e);
            throw new BusinessException(ResultCode.ERROR, "文件上传失败");
        }
    }
    
    @Override
    public boolean deleteFile(String fileUrl) {
        if (!StringUtils.hasText(fileUrl)) {
            return false;
        }
        
        try {
            // 提取相对路径
            String relativePath = fileUrl.replace(uploadDomain + "/uploads/", "");
            Path filePath = Paths.get(uploadPath, relativePath);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("文件删除成功: {}", fileUrl);
                return true;
            }
            
        } catch (IOException e) {
            log.error("文件删除失败: {}", fileUrl, e);
        }
        
        return false;
    }
    
    @Override
    public boolean validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        
        // 检查文件大小
        if (file.getSize() > maxFileSize) {
            return false;
        }
        
        // 检查文件扩展名
        String extension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_IMAGE_TYPES.contains(extension.toLowerCase())) {
            return false;
        }
        
        // 检查MIME类型
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    
    @Override
    public String getFileExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "";
        }
        
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        
        return "";
    }
    
    /**
     * 调整图片尺寸
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        
        // 设置渲染质量
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        
        return resizedImage;
    }
}
