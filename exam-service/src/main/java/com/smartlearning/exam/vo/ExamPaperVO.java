package com.smartlearning.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷响应VO
 */
@Data
public class ExamPaperVO {
    
    /**
     * 试卷ID
     */
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
     * 课程标题
     */
    private String courseTitle;
    
    /**
     * 试卷类型
     */
    private String type;
    
    /**
     * 试卷难度
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
     * 试卷状态
     */
    private String status;
    
    /**
     * 是否随机出题
     */
    private Integer isRandom;
    
    /**
     * 是否打乱题目顺序
     */
    private Integer isShuffled;
    
    /**
     * 是否显示答案
     */
    private Integer showAnswer;
    
    /**
     * 允许重考次数
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
     * 创建者信息
     */
    private CreatorInfo creator;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 题目列表
     */
    private List<QuestionVO> questions;
    
    /**
     * 创建者信息
     */
    @Data
    public static class CreatorInfo {
        private Long id;
        private String username;
        private String nickname;
    }
}
