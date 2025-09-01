package com.smartlearning.learning.service;

import com.smartlearning.learning.document.LearningBehaviorDocument;
import com.smartlearning.learning.document.LearningNoteDocument;
import com.smartlearning.learning.document.UserLearningStatsDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB学习服务接口
 */
public interface MongoLearningService {
    
    // ==================== 学习笔记相关 ====================
    
    /**
     * 保存学习笔记
     */
    LearningNoteDocument saveLearningNote(LearningNoteDocument note);
    
    /**
     * 根据ID查询笔记
     */
    Optional<LearningNoteDocument> findNoteById(String id);
    
    /**
     * 分页查询用户笔记
     */
    Page<LearningNoteDocument> findUserNotes(Long userId, Pageable pageable);
    
    /**
     * 查询课程笔记
     */
    List<LearningNoteDocument> findCourseNotes(Long userId, Long courseId);
    
    /**
     * 查询公开笔记
     */
    Page<LearningNoteDocument> findPublicNotes(Long courseId, Pageable pageable);
    
    /**
     * 搜索笔记
     */
    Page<LearningNoteDocument> searchNotes(String keyword, Pageable pageable);
    
    /**
     * 点赞笔记
     */
    boolean likeNote(String noteId);
    
    /**
     * 删除笔记
     */
    boolean deleteNote(String noteId, Long userId);
    
    // ==================== 学习行为相关 ====================
    
    /**
     * 记录学习行为
     */
    LearningBehaviorDocument recordLearningBehavior(LearningBehaviorDocument behavior);
    
    /**
     * 查询用户学习行为
     */
    Page<LearningBehaviorDocument> findUserBehaviors(Long userId, Pageable pageable);
    
    /**
     * 查询时间范围内的学习行为
     */
    List<LearningBehaviorDocument> findBehaviorsByTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计学习时长
     */
    Integer calculateLearningDuration(Long userId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 分析学习习惯
     */
    List<LearningBehaviorDocument> analyzeLearningHabits(Long userId, LocalDate startDate, LocalDate endDate);
    
    // ==================== 学习统计相关 ====================
    
    /**
     * 保存学习统计
     */
    UserLearningStatsDocument saveLearningStats(UserLearningStatsDocument stats);
    
    /**
     * 查询用户统计数据
     */
    Optional<UserLearningStatsDocument> findUserStats(Long userId, LocalDate date);
    
    /**
     * 查询最近统计数据
     */
    List<UserLearningStatsDocument> findRecentStats(Long userId, int days);
    
    /**
     * 查询学习排行榜
     */
    Page<UserLearningStatsDocument> findLearningRanking(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * 生成每日统计
     */
    UserLearningStatsDocument generateDailyStats(Long userId, LocalDate date);
    
    /**
     * 更新学习统计
     */
    boolean updateLearningStats(Long userId, LocalDate date, Integer duration, Integer lessons);
}
