package com.smartlearning.analytics.service;

import com.smartlearning.analytics.entity.LearningAnalytics;
import com.smartlearning.analytics.entity.UserBehavior;

import java.util.List;

/**
 * 数据收集服务接口
 */
public interface DataCollectionService {
    
    /**
     * 收集学习行为数据
     */
    void collectLearningData(LearningAnalytics learningData);
    
    /**
     * 批量收集学习行为数据
     */
    void batchCollectLearningData(List<LearningAnalytics> learningDataList);
    
    /**
     * 收集用户行为数据
     */
    void collectUserBehavior(UserBehavior behaviorData);
    
    /**
     * 批量收集用户行为数据
     */
    void batchCollectUserBehavior(List<UserBehavior> behaviorDataList);
    
    /**
     * 收集页面访问数据
     */
    void collectPageView(Long userId, String pagePath, String pageTitle, String referrer, Integer stayTime);
    
    /**
     * 收集事件数据
     */
    void collectEvent(Long userId, String eventType, String eventAction, String eventLabel, Integer eventValue);
}
