package com.smartlearning.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷实体类
 */
@Data
@TableName("exam_papers")
public class ExamPaper {
    
    /**
     * 试卷ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 试卷标题
     */
    private String title;
    
    /**
     * 试卷描述
     */
    private String description;
    
    /**
     * 所属课程ID
     */
    private Long courseId;
    
    /**
     * 试卷类型 PRACTICE:练习 EXAM:考试 HOMEWORK:作业
     */
    private String type;
    
    /**
     * 试卷难度 EASY:简单 MEDIUM:中等 HARD:困难
     */
    private String difficulty;
    
    /**
     * 总分
     */
    private Integer totalScore;
    
    /**
     * 及格分数
     */
    private Integer passScore;
    
    /**
     * 考试时长(分钟)
     */
    private Integer duration;
    
    /**
     * 题目数量
     */
    private Integer questionCount;
    
    /**
     * 试卷状态 DRAFT:草稿 PUBLISHED:已发布 ARCHIVED:已归档
     */
    private String status;
    
    /**
     * 是否随机出题 0:否 1:是
     */
    private Integer isRandom;
    
    /**
     * 是否打乱题目顺序 0:否 1:是
     */
    private Integer isShuffled;
    
    /**
     * 是否显示答案 0:否 1:是
     */
    private Integer showAnswer;
    
    /**
     * 允许重考次数 -1:无限制
     */
    private Integer retakeLimit;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 参考人数
     */
    private Integer participantCount;
    
    /**
     * 平均分
     */
    private Double averageScore;
    
    /**
     * 通过率
     */
    private Double passRate;
    
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
