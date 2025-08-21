package com.smartlearning.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户资料实体类
 */
@Data
@TableName("user_profiles")
public class UserProfile {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 身份证号
     */
    private String idCard;
    
    /**
     * 学历
     */
    private String education;
    
    /**
     * 职业
     */
    private String profession;
    
    /**
     * 公司
     */
    private String company;
    
    /**
     * 个人简介
     */
    private String bio;
    
    /**
     * 兴趣标签
     */
    @TableField(typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private List<String> interests;
    
    /**
     * 学习目标
     */
    private String learningGoals;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
