package com.smartlearning.learning.repository;

import com.smartlearning.learning.document.LearningBehaviorDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学习行为MongoDB Repository
 */
@Repository
public interface LearningBehaviorRepository extends MongoRepository<LearningBehaviorDocument, String> {
    
    /**
     * 根据用户ID查询学习行为
     */
    Page<LearningBehaviorDocument> findByUserIdOrderByActionTimeDesc(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和时间范围查询学习行为
     */
    List<LearningBehaviorDocument> findByUserIdAndActionTimeBetweenOrderByActionTimeDesc(
            Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据课程ID查询学习行为
     */
    Page<LearningBehaviorDocument> findByCourseIdOrderByActionTimeDesc(Long courseId, Pageable pageable);
    
    /**
     * 根据行为类型查询
     */
    List<LearningBehaviorDocument> findByUserIdAndActionTypeAndActionTimeBetween(
            Long userId, Integer actionType, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计用户总学习时长
     */
    @Query(value = "{'userId': ?0, 'actionTime': {$gte: ?1, $lte: ?2}}", 
           fields = "{'duration': 1}")
    List<LearningBehaviorDocument> findUserLearningDuration(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计课程学习数据
     */
    @Query("{'courseId': ?0, 'actionTime': {$gte: ?1, $lte: ?2}}")
    List<LearningBehaviorDocument> findCourseLearningData(Long courseId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询用户设备使用情况
     */
    @Query(value = "{'userId': ?0, 'actionTime': {$gte: ?1, $lte: ?2}}", 
           fields = "{'deviceType': 1, 'browser': 1, 'os': 1}")
    List<LearningBehaviorDocument> findUserDeviceUsage(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询学习活跃时段
     */
    @Query("{'userId': ?0, 'actionTime': {$gte: ?1, $lte: ?2}}")
    List<LearningBehaviorDocument> findUserActiveHours(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计不同行为类型的数量
     */
    @Query(value = "{'userId': ?0, 'actionTime': {$gte: ?1, $lte: ?2}}", 
           count = true)
    long countByUserIdAndActionTimeBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);
}
