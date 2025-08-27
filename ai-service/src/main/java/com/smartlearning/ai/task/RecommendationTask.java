package com.smartlearning.ai.task;

import com.smartlearning.ai.service.AiRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 推荐系统定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationTask {
    
    private final AiRecommendationService aiRecommendationService;
    
    /**
     * 每天凌晨2点更新推荐模型
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateRecommendationModel() {
        log.info("开始执行定时任务：更新推荐模型");
        
        try {
            boolean success = aiRecommendationService.updateRecommendationModel();
            log.info("推荐模型更新完成，结果：{}", success ? "成功" : "失败");
            
        } catch (Exception e) {
            log.error("推荐模型更新失败", e);
        }
    }
    
    /**
     * 每周日凌晨3点清理过期数据（保留90天）
     */
    @Scheduled(cron = "0 0 3 * * SUN")
    public void cleanExpiredData() {
        log.info("开始执行定时任务：清理过期数据");
        
        try {
            int deletedCount = aiRecommendationService.cleanExpiredData(90);
            log.info("过期数据清理完成，删除记录数：{}", deletedCount);
            
        } catch (Exception e) {
            log.error("过期数据清理失败", e);
        }
    }
    
    /**
     * 每小时统计推荐效果（可用于监控）
     */
    @Scheduled(fixedRate = 3600000) // 1小时 = 3600000毫秒
    public void monitorRecommendationPerformance() {
        try {
            // 获取最近24小时的推荐效果统计
            var stats = aiRecommendationService.getRecommendationStats(1);
            
            // 这里可以添加监控逻辑，比如发送告警等
            log.debug("推荐效果监控：{}", stats);
            
        } catch (Exception e) {
            log.error("推荐效果监控失败", e);
        }
    }
}
