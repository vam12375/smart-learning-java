package com.smartlearning.analytics.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 学习报告请求DTO
 */
@Data
public class LearningReportRequest {
    
    /**
     * 用户ID（可选，不传则生成全平台报告）
     */
    private Long userId;
    
    /**
     * 课程ID（可选，不传则包含所有课程）
     */
    private Long courseId;
    
    /**
     * 开始日期
     */
    private LocalDate startDate;
    
    /**
     * 结束日期
     */
    private LocalDate endDate;
    
    /**
     * 报告类型：user-用户报告，course-课程报告，platform-平台报告
     */
    private String reportType = "user";
    
    /**
     * 统计粒度：day-按天，week-按周，month-按月
     */
    private String granularity = "day";
    
    /**
     * 是否包含详细数据
     */
    private Boolean includeDetails = false;
    
    /**
     * 导出格式：json, excel, pdf
     */
    private String exportFormat = "json";
}
