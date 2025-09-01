package com.smartlearning.learning.repository;

import com.smartlearning.learning.document.UserLearningStatsDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 用户学习统计MongoDB Repository
 */
@Repository
public interface UserLearningStatsRepository extends MongoRepository<UserLearningStatsDocument, String> {
    
    /**
     * 根据用户ID和日期查询统计数据
     */
    Optional<UserLearningStatsDocument> findByUserIdAndStatDate(Long userId, LocalDate statDate);
    
    /**
     * 根据用户ID查询统计数据（按日期倒序）
     */
    List<UserLearningStatsDocument> findByUserIdOrderByStatDateDesc(Long userId);
    
    /**
     * 根据用户ID和日期范围查询统计数据
     */
    List<UserLearningStatsDocument> findByUserIdAndStatDateBetweenOrderByStatDateDesc(
            Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 查询用户最近N天的统计数据
     */
    @Query("{'userId': ?0, 'statDate': {$gte: ?1}}")
    List<UserLearningStatsDocument> findRecentStats(Long userId, LocalDate startDate);
    
    /**
     * 查询学习排行榜（按总学习时长）
     */
    @Query(value = "{'statDate': {$gte: ?0, $lte: ?1}}", 
           sort = "{'totalLearningTime': -1}")
    Page<UserLearningStatsDocument> findLearningRanking(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * 查询连续学习天数排行榜
     */
    @Query(value = "{'statDate': ?0}", 
           sort = "{'consecutiveDays': -1}")
    Page<UserLearningStatsDocument> findConsecutiveDaysRanking(LocalDate date, Pageable pageable);
    
    /**
     * 统计总学习时长
     */
    @Query(value = "{'userId': ?0, 'statDate': {$gte: ?1, $lte: ?2}}")
    List<UserLearningStatsDocument> findTotalLearningTime(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 查询学习目标完成情况
     */
    @Query("{'userId': ?0, 'statDate': {$gte: ?1, $lte: ?2}}")
    List<UserLearningStatsDocument> findGoalProgress(Long userId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 查询活跃学习者
     */
    @Query(value = "{'statDate': {$gte: ?0, $lte: ?1}, 'totalLearningTime': {$gt: ?2}}", 
           sort = "{'totalLearningTime': -1}")
    List<UserLearningStatsDocument> findActiveLearners(LocalDate startDate, LocalDate endDate, Integer minLearningTime);
    
    /**
     * 统计平台整体学习数据
     */
    @Query("{'statDate': {$gte: ?0, $lte: ?1}}")
    List<UserLearningStatsDocument> findPlatformStats(LocalDate startDate, LocalDate endDate);
}
