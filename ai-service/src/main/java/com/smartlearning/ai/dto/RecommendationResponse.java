package com.smartlearning.ai.dto;

import lombok.Data;

import java.util.List;

/**
 * 推荐响应DTO
 */
@Data
public class RecommendationResponse {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 推荐课程列表
     */
    private List<RecommendedCourse> recommendations;
    
    /**
     * 推荐算法类型
     */
    private String algorithmType;
    
    /**
     * 推荐批次ID
     */
    private String batchId;
    
    /**
     * 推荐课程信息
     */
    @Data
    public static class RecommendedCourse {
        
        /**
         * 课程ID
         */
        private Long courseId;
        
        /**
         * 课程标题
         */
        private String title;
        
        /**
         * 课程描述
         */
        private String description;
        
        /**
         * 课程封面
         */
        private String coverImage;
        
        /**
         * 讲师姓名
         */
        private String teacherName;
        
        /**
         * 课程价格
         */
        private Double price;
        
        /**
         * 课程评分
         */
        private Double rating;
        
        /**
         * 学生数量
         */
        private Integer studentCount;
        
        /**
         * 推荐分数
         */
        private Double score;
        
        /**
         * 推荐原因
         */
        private String reason;
        
        /**
         * 推荐位置
         */
        private Integer position;
    }
}
