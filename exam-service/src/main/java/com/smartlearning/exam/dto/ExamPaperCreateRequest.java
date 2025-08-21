package com.smartlearning.exam.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷创建请求DTO
 */
@Data
public class ExamPaperCreateRequest {
    
    /**
     * 试卷标题
     */
    @NotBlank(message = "试卷标题不能为空")
    @Size(max = 200, message = "试卷标题长度不能超过200个字符")
    private String title;
    
    /**
     * 试卷描述
     */
    @Size(max = 1000, message = "试卷描述长度不能超过1000个字符")
    private String description;
    
    /**
     * 所属课程ID
     */
    private Long courseId;
    
    /**
     * 试卷类型
     */
    @NotBlank(message = "试卷类型不能为空")
    @Pattern(regexp = "^(PRACTICE|EXAM|HOMEWORK)$", message = "试卷类型不正确")
    private String type;
    
    /**
     * 试卷难度
     */
    @NotBlank(message = "试卷难度不能为空")
    @Pattern(regexp = "^(EASY|MEDIUM|HARD)$", message = "试卷难度不正确")
    private String difficulty;
    
    /**
     * 总分
     */
    @NotNull(message = "总分不能为空")
    private Integer totalScore;
    
    /**
     * 及格分数
     */
    @NotNull(message = "及格分数不能为空")
    private Integer passScore;
    
    /**
     * 考试时长(分钟)
     */
    @NotNull(message = "考试时长不能为空")
    private Integer duration;
    
    /**
     * 是否随机出题
     */
    private Integer isRandom = 0;
    
    /**
     * 是否打乱题目顺序
     */
    private Integer isShuffled = 0;
    
    /**
     * 是否显示答案
     */
    private Integer showAnswer = 0;
    
    /**
     * 允许重考次数
     */
    private Integer retakeLimit = 1;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 题目列表
     */
    private List<QuestionItem> questions;
    
    /**
     * 题目项
     */
    @Data
    public static class QuestionItem {
        private Long questionId;
        private Integer score;
        private Integer order;
    }
}
