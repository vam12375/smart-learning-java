package com.smartlearning.exam.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 试卷题目关联实体类
 */
@Data
@TableName("paper_questions")
public class PaperQuestion {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 试卷ID
     */
    private Long paperId;
    
    /**
     * 题目ID
     */
    private Long questionId;
    
    /**
     * 题目序号
     */
    private Integer questionOrder;
    
    /**
     * 题目分值
     */
    private Integer score;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 逻辑删除标识
     */
    @TableLogic
    private Integer deleted;
}
