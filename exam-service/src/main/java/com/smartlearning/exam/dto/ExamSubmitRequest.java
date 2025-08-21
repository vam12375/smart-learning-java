package com.smartlearning.exam.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 考试提交请求DTO
 */
@Data
public class ExamSubmitRequest {
    
    /**
     * 考试记录ID
     */
    @NotNull(message = "考试记录ID不能为空")
    private Long recordId;
    
    /**
     * 答题列表
     */
    private List<AnswerItem> answers;
    
    /**
     * 答题项
     */
    @Data
    public static class AnswerItem {
        /**
         * 题目ID
         */
        private Long questionId;
        
        /**
         * 用户答案
         */
        private String userAnswer;
        
        /**
         * 答题时间(秒)
         */
        private Integer duration;
    }
}
