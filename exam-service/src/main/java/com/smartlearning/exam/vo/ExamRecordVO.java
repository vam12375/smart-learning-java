package com.smartlearning.exam.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试记录响应VO
 */
@Data
public class ExamRecordVO {
    
    /**
     * 记录ID
     */
    private Long id;
    
    /**
     * 试卷ID
     */
    private Long paperId;
    
    /**
     * 试卷标题
     */
    private String paperTitle;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户姓名
     */
    private String userName;
    
    /**
     * 考试状态
     */
    private String status;
    
    /**
     * 总分
     */
    private Integer totalScore;
    
    /**
     * 得分
     */
    private Integer score;
    
    /**
     * 正确题数
     */
    private Integer correctCount;
    
    /**
     * 错误题数
     */
    private Integer wrongCount;
    
    /**
     * 用时(秒)
     */
    private Integer duration;
    
    /**
     * 是否通过
     */
    private Integer isPassed;
    
    /**
     * 排名
     */
    private Integer ranking;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    
    /**
     * 阅卷时间
     */
    private LocalDateTime gradeTime;
    
    /**
     * 阅卷者ID
     */
    private Long graderId;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 答题详情
     */
    private List<AnswerDetail> answers;
    
    /**
     * 答题详情
     */
    @Data
    public static class AnswerDetail {
        private Long questionId;
        private String questionContent;
        private String questionType;
        private String userAnswer;
        private String correctAnswer;
        private Integer isCorrect;
        private Integer score;
        private Integer totalScore;
        private String comment;
    }
}
