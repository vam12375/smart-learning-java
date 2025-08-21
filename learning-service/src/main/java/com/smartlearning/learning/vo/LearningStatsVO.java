package com.smartlearning.learning.vo;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 学习统计响应VO
 */
@Data
public class LearningStatsVO {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 总学习时长(分钟)
     */
    private Integer totalDuration;
    
    /**
     * 总完成课时数
     */
    private Integer totalLessons;
    
    /**
     * 总学习课程数
     */
    private Integer totalCourses;
    
    /**
     * 总笔记数
     */
    private Integer totalNotes;
    
    /**
     * 连续学习天数
     */
    private Integer continuousDays;
    
    /**
     * 总积分
     */
    private Integer totalPoints;
    
    /**
     * 本周学习时长
     */
    private Integer weekDuration;
    
    /**
     * 本月学习时长
     */
    private Integer monthDuration;
    
    /**
     * 今日学习时长
     */
    private Integer todayDuration;
    
    /**
     * 学习排名
     */
    private Integer ranking;
    
    /**
     * 最近7天学习数据
     */
    private List<DailyStats> recentDays;
    
    /**
     * 每日学习统计
     */
    @Data
    public static class DailyStats {
        private LocalDate date;
        private Integer duration;
        private Integer lessons;
        private Integer points;
    }
}
