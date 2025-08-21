package com.smartlearning.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 题目实体类
 */
@Data
@TableName("questions")
public class Question {
    
    /**
     * 题目ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 题目类型 SINGLE:单选 MULTIPLE:多选 JUDGE:判断 FILL:填空 ESSAY:问答
     */
    private String type;
    
    /**
     * 题目内容
     */
    private String content;
    
    /**
     * 题目选项(JSON格式)
     */
    private String options;
    
    /**
     * 正确答案
     */
    private String answer;
    
    /**
     * 答案解析
     */
    private String explanation;
    
    /**
     * 题目难度 EASY:简单 MEDIUM:中等 HARD:困难
     */
    private String difficulty;
    
    /**
     * 题目分值
     */
    private Integer score;
    
    /**
     * 所属课程ID
     */
    private Long courseId;
    
    /**
     * 所属章节ID
     */
    private Long chapterId;
    
    /**
     * 知识点标签
     */
    private String tags;
    
    /**
     * 题目状态 0:禁用 1:启用
     */
    private Integer status;
    
    /**
     * 使用次数
     */
    private Integer useCount;
    
    /**
     * 正确率
     */
    private Double accuracy;
    
    /**
     * 创建者ID
     */
    private Long creatorId;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除标识
     */
    @TableLogic
    private Integer deleted;
}
