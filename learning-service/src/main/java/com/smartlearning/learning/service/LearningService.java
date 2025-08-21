package com.smartlearning.learning.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartlearning.learning.dto.LearningNoteRequest;
import com.smartlearning.learning.dto.LearningProgressRequest;
import com.smartlearning.learning.entity.CourseFavorite;
import com.smartlearning.learning.entity.LearningNote;
import com.smartlearning.learning.entity.LearningProgress;
import com.smartlearning.learning.vo.LearningProgressVO;
import com.smartlearning.learning.vo.LearningStatsVO;

import java.util.List;

/**
 * 学习服务接口
 */
public interface LearningService {
    
    /**
     * 更新学习进度
     */
    LearningProgress updateProgress(Long userId, LearningProgressRequest request);
    
    /**
     * 获取用户学习进度
     */
    LearningProgress getUserProgress(Long userId, Long lessonId);
    
    /**
     * 获取课程学习进度
     */
    List<LearningProgressVO> getCourseProgress(Long userId, Long courseId);
    
    /**
     * 分页查询学习进度
     */
    IPage<LearningProgressVO> getUserProgressPage(Long userId, Integer page, Integer size);
    
    /**
     * 获取最近学习
     */
    List<LearningProgressVO> getRecentLearning(Long userId, Integer limit);
    
    /**
     * 创建学习笔记
     */
    LearningNote createNote(Long userId, LearningNoteRequest request);
    
    /**
     * 更新学习笔记
     */
    LearningNote updateNote(Long userId, Long noteId, LearningNoteRequest request);
    
    /**
     * 删除学习笔记
     */
    boolean deleteNote(Long userId, Long noteId);
    
    /**
     * 获取笔记详情
     */
    LearningNote getNoteById(Long noteId);
    
    /**
     * 分页查询用户笔记
     */
    IPage<LearningNote> getUserNotesPage(Long userId, Integer page, Integer size);
    
    /**
     * 获取课程笔记
     */
    List<LearningNote> getCourseNotes(Long userId, Long courseId);
    
    /**
     * 获取课时笔记
     */
    List<LearningNote> getLessonNotes(Long userId, Long lessonId);
    
    /**
     * 搜索笔记
     */
    IPage<LearningNote> searchNotes(Long userId, String keyword, Integer page, Integer size);
    
    /**
     * 收藏课程
     */
    CourseFavorite favoriteCourse(Long userId, Long courseId, String category, String remark);
    
    /**
     * 取消收藏
     */
    boolean unfavoriteCourse(Long userId, Long courseId);
    
    /**
     * 检查是否已收藏
     */
    boolean isFavorited(Long userId, Long courseId);
    
    /**
     * 分页查询收藏列表
     */
    IPage<CourseFavorite> getFavoritesPage(Long userId, Integer page, Integer size);
    
    /**
     * 根据分组查询收藏
     */
    List<CourseFavorite> getFavoritesByCategory(Long userId, String category);
    
    /**
     * 获取收藏分组
     */
    List<String> getFavoriteCategories(Long userId);
    
    /**
     * 获取学习统计
     */
    LearningStatsVO getLearningStats(Long userId);
    
    /**
     * 更新每日学习统计
     */
    boolean updateDailyStats(Long userId);
    
    /**
     * 点赞笔记
     */
    boolean likeNote(Long noteId);
    
    /**
     * 取消点赞
     */
    boolean unlikeNote(Long noteId);
}
