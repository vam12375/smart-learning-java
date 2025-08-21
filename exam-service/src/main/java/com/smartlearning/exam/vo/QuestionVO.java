package com.smartlearning.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 题目响应VO
 */
@Data
public class QuestionVO {
    
    /**
     * 题目ID
     */
    private Long id;
    
    /**
     * 题目类型
     */
    private String type;
    
    /**
     * 题目内容
     */
    private String content;
    
    /**
     * 题目选项
     */
    private List<String> options;
    
    /**
     * 正确答案(考试时不显示)
     */
    private String answer;
    
    /**
     * 答案解析(考试时不显示)
     */
    private String explanation;
    
    /**
     * 题目难度
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
    private List<String> tags;
    
    /**
     * 题目状态
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
     * 题目序号(在试卷中的序号)
     */
    private Integer questionOrder;
    
    /**
     * 用户答案(答题时填写)
     */
    private String userAnswer;
    
    /**
     * 是否正确(阅卷后显示)
     */
    private Integer isCorrect;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
