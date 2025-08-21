package com.smartlearning.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 考试记录实体类
 */
@Data
@TableName("exam_records")
public class ExamRecord {
    
    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 试卷ID
     */
    private Long paperId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户姓名
     */
    private String userName;
    
    /**
     * 考试状态 STARTED:已开始 SUBMITTED:已提交 TIMEOUT:超时 GRADED:已阅卷
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
     * 是否通过 0:未通过 1:通过
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
