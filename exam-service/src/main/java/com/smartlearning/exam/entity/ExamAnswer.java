package com.smartlearning.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 答题记录实体类
 */
@Data
@TableName("exam_answers")
public class ExamAnswer {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 考试记录ID
     */
    private Long recordId;
    
    /**
     * 题目ID
     */
    private Long questionId;
    
    /**
     * 用户答案
     */
    private String userAnswer;
    
    /**
     * 正确答案
     */
    private String correctAnswer;
    
    /**
     * 是否正确 0:错误 1:正确
     */
    private Integer isCorrect;
    
    /**
     * 得分
     */
    private Integer score;
    
    /**
     * 题目分值
     */
    private Integer totalScore;
    
    /**
     * 答题时间(秒)
     */
    private Integer duration;
    
    /**
     * 教师评语
     */
    private String comment;
    
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
