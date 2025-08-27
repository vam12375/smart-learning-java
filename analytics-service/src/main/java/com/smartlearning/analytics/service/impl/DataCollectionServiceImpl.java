package com.smartlearning.analytics.service.impl;

import com.smartlearning.analytics.entity.LearningAnalytics;
import com.smartlearning.analytics.entity.UserBehavior;
import com.smartlearning.analytics.service.DataCollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 数据收集服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataCollectionServiceImpl implements DataCollectionService {
    
    @Override
    public void collectLearningData(LearningAnalytics learningData) {
        log.info("收集学习行为数据: userId={}, courseId={}, actionType={}", 
                learningData.getUserId(), learningData.getCourseId(), learningData.getActionType());
        
        // 设置默认值
        if (learningData.getCreateTime() == null) {
            learningData.setCreateTime(LocalDateTime.now());
        }
        if (learningData.getUpdateTime() == null) {
            learningData.setUpdateTime(LocalDateTime.now());
        }
        if (learningData.getDeleted() == null) {
            learningData.setDeleted(false);
        }
        
        // 这里应该保存到数据库
        // learningAnalyticsMapper.insert(learningData);
        
        // 同时可以发送到消息队列进行实时处理
        // messageProducer.sendLearningData(learningData);
        
        log.info("学习行为数据收集成功: id={}", learningData.getId());
    }
    
    @Override
    public void batchCollectLearningData(List<LearningAnalytics> learningDataList) {
        log.info("批量收集学习行为数据: count={}", learningDataList.size());
        
        for (LearningAnalytics learningData : learningDataList) {
            if (learningData.getCreateTime() == null) {
                learningData.setCreateTime(LocalDateTime.now());
            }
            if (learningData.getUpdateTime() == null) {
                learningData.setUpdateTime(LocalDateTime.now());
            }
            if (learningData.getDeleted() == null) {
                learningData.setDeleted(false);
            }
        }
        
        // 批量保存到数据库
        // learningAnalyticsMapper.insertBatch(learningDataList);
        
        log.info("批量学习行为数据收集成功: count={}", learningDataList.size());
    }
    
    @Override
    public void collectUserBehavior(UserBehavior behaviorData) {
        log.info("收集用户行为数据: userId={}, behaviorType={}, pagePath={}", 
                behaviorData.getUserId(), behaviorData.getBehaviorType(), behaviorData.getPagePath());
        
        // 设置默认值
        if (behaviorData.getSessionId() == null) {
            behaviorData.setSessionId(UUID.randomUUID().toString());
        }
        if (behaviorData.getBehaviorTime() == null) {
            behaviorData.setBehaviorTime(LocalDateTime.now());
        }
        if (behaviorData.getCreateTime() == null) {
            behaviorData.setCreateTime(LocalDateTime.now());
        }
        if (behaviorData.getDeleted() == null) {
            behaviorData.setDeleted(false);
        }
        
        // 这里应该保存到数据库
        // userBehaviorMapper.insert(behaviorData);
        
        // 实时分析用户行为
        // realtimeAnalysisService.analyzeUserBehavior(behaviorData);
        
        log.info("用户行为数据收集成功: id={}", behaviorData.getId());
    }
    
    @Override
    public void batchCollectUserBehavior(List<UserBehavior> behaviorDataList) {
        log.info("批量收集用户行为数据: count={}", behaviorDataList.size());
        
        for (UserBehavior behaviorData : behaviorDataList) {
            if (behaviorData.getSessionId() == null) {
                behaviorData.setSessionId(UUID.randomUUID().toString());
            }
            if (behaviorData.getBehaviorTime() == null) {
                behaviorData.setBehaviorTime(LocalDateTime.now());
            }
            if (behaviorData.getCreateTime() == null) {
                behaviorData.setCreateTime(LocalDateTime.now());
            }
            if (behaviorData.getDeleted() == null) {
                behaviorData.setDeleted(false);
            }
        }
        
        // 批量保存到数据库
        // userBehaviorMapper.insertBatch(behaviorDataList);
        
        log.info("批量用户行为数据收集成功: count={}", behaviorDataList.size());
    }
    
    @Override
    public void collectPageView(Long userId, String pagePath, String pageTitle, String referrer, Integer stayTime) {
        log.info("收集页面访问数据: userId={}, pagePath={}", userId, pagePath);
        
        UserBehavior behaviorData = new UserBehavior();
        behaviorData.setUserId(userId);
        behaviorData.setPagePath(pagePath);
        behaviorData.setPageTitle(pageTitle);
        behaviorData.setReferrer(referrer);
        behaviorData.setStayTime(stayTime);
        behaviorData.setBehaviorType(1); // 页面访问
        behaviorData.setBehaviorDesc("页面访问: " + pageTitle);
        
        collectUserBehavior(behaviorData);
    }
    
    @Override
    public void collectEvent(Long userId, String eventType, String eventAction, String eventLabel, Integer eventValue) {
        log.info("收集事件数据: userId={}, eventType={}, eventAction={}", userId, eventType, eventAction);
        
        UserBehavior behaviorData = new UserBehavior();
        behaviorData.setUserId(userId);
        behaviorData.setBehaviorType(getBehaviorTypeByEvent(eventType));
        behaviorData.setBehaviorDesc(String.format("%s: %s", eventType, eventAction));
        
        if (eventLabel != null) {
            behaviorData.setTargetType(eventLabel);
        }
        if (eventValue != null) {
            behaviorData.setTargetId(eventValue.longValue());
        }
        
        collectUserBehavior(behaviorData);
    }
    
    /**
     * 根据事件类型获取行为类型
     */
    private Integer getBehaviorTypeByEvent(String eventType) {
        switch (eventType.toLowerCase()) {
            case "click":
                return 2; // 点击
            case "search":
                return 3; // 搜索
            case "download":
                return 4; // 下载
            case "share":
                return 5; // 分享
            default:
                return 1; // 默认为页面访问
        }
    }
}
