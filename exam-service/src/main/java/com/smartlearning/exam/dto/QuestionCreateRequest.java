package com.smartlearning.exam.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 题目创建请求DTO
 */
@Data
public class QuestionCreateRequest {
    
    /**
     * 题目类型
     */
    @NotBlank(message = "题目类型不能为空")
    @Pattern(regexp = "^(SINGLE|MULTIPLE|JUDGE|FILL|ESSAY)$", message = "题目类型不正确")
    private String type;
    
    /**
     * 题目内容
     */
    @NotBlank(message = "题目内容不能为空")
    @Size(max = 2000, message = "题目内容长度不能超过2000个字符")
    private String content;
    
    /**
     * 题目选项
     */
    private List<String> options;
    
    /**
     * 正确答案
     */
    @NotBlank(message = "正确答案不能为空")
    private String answer;
    
    /**
     * 答案解析
     */
    @Size(max = 1000, message = "答案解析长度不能超过1000个字符")
    private String explanation;
    
    /**
     * 题目难度
     */
    @NotBlank(message = "题目难度不能为空")
    @Pattern(regexp = "^(EASY|MEDIUM|HARD)$", message = "题目难度不正确")
    private String difficulty;
    
    /**
     * 题目分值
     */
    @NotNull(message = "题目分值不能为空")
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
    private List<String> tags;
}
