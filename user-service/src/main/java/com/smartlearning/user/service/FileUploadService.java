package com.smartlearning.user.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务接口
 */
public interface FileUploadService {
    
    /**
     * 上传头像
     */
    String uploadAvatar(MultipartFile file, Long userId);
    
    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file, String directory);
    
    /**
     * 删除文件
     */
    boolean deleteFile(String fileUrl);
    
    /**
     * 验证图片文件
     */
    boolean validateImageFile(MultipartFile file);
    
    /**
     * 获取文件扩展名
     */
    String getFileExtension(String filename);
}
