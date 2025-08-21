package com.smartlearning.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.common.exception.BusinessException;
import com.smartlearning.common.result.ResultCode;
import com.smartlearning.learning.dto.LearningNoteRequest;
import com.smartlearning.learning.dto.LearningProgressRequest;
import com.smartlearning.learning.entity.CourseFavorite;
import com.smartlearning.learning.entity.LearningNote;
import com.smartlearning.learning.entity.LearningProgress;
import com.smartlearning.learning.entity.LearningStats;
import com.smartlearning.learning.mapper.*;
import com.smartlearning.learning.service.LearningService;
import com.smartlearning.learning.vo.LearningProgressVO;
import com.smartlearning.learning.vo.LearningStatsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 学习服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {
    
    private final LearningProgressMapper progressMapper;
    private final LearningNoteMapper noteMapper;
    private final CourseFavoriteMapper favoriteMapper;
    private final LearningStatsMapper statsMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LearningProgress updateProgress(Long userId, LearningProgressRequest request) {
        log.info("更新学习进度: userId={}, lessonId={}, progress={}", 
                userId, request.getLessonId(), request.getProgress());
        
        // 查询现有进度
        LearningProgress existingProgress = progressMapper.selectByUserAndLesson(userId, request.getLessonId());
        
        if (existingProgress == null) {
            // 创建新的学习进度
            LearningProgress progress = new LearningProgress();
            BeanUtils.copyProperties(request, progress);
            progress.setUserId(userId);
            progress.setLearnCount(1);
            progress.setFirstLearnTime(LocalDateTime.now());
            progress.setLastLearnTime(LocalDateTime.now());
            progress.setCreateTime(LocalDateTime.now());
            progress.setUpdateTime(LocalDateTime.now());
            
            if (request.getIsCompleted() != null && request.getIsCompleted() == 1) {
                progress.setCompleteTime(LocalDateTime.now());
            }
            
            int result = progressMapper.insert(progress);
            if (result <= 0) {
                throw new BusinessException(ResultCode.ERROR, "学习进度创建失败");
            }
            
            log.info("学习进度创建成功: progressId={}", progress.getId());
            return progress;
        } else {
            // 更新现有进度
            existingProgress.setProgress(request.getProgress());
            existingProgress.setDuration(request.getDuration());
            existingProgress.setLastPosition(request.getLastPosition());
            existingProgress.setLearnCount(existingProgress.getLearnCount() + 1);
            existingProgress.setLastLearnTime(LocalDateTime.now());
            existingProgress.setUpdateTime(LocalDateTime.now());
            
            if (request.getIsCompleted() != null && request.getIsCompleted() == 1 
                && existingProgress.getIsCompleted() != 1) {
                existingProgress.setIsCompleted(1);
                existingProgress.setCompleteTime(LocalDateTime.now());
            }
            
            int result = progressMapper.updateById(existingProgress);
            if (result <= 0) {
                throw new BusinessException(ResultCode.ERROR, "学习进度更新失败");
            }
            
            log.info("学习进度更新成功: progressId={}", existingProgress.getId());
            return existingProgress;
        }
    }
    
    @Override
    public LearningProgress getUserProgress(Long userId, Long lessonId) {
        return progressMapper.selectByUserAndLesson(userId, lessonId);
    }
    
    @Override
    public List<LearningProgressVO> getCourseProgress(Long userId, Long courseId) {
        return progressMapper.selectCourseProgress(userId, courseId);
    }
    
    @Override
    public IPage<LearningProgressVO> getUserProgressPage(Long userId, Integer page, Integer size) {
        Page<LearningProgressVO> pageParam = new Page<>(page, size);
        return progressMapper.selectUserProgressPage(pageParam, userId);
    }
    
    @Override
    public List<LearningProgressVO> getRecentLearning(Long userId, Integer limit) {
        return progressMapper.selectRecentLearning(userId, limit);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LearningNote createNote(Long userId, LearningNoteRequest request) {
        log.info("创建学习笔记: userId={}, lessonId={}, title={}", 
                userId, request.getLessonId(), request.getTitle());
        
        LearningNote note = new LearningNote();
        BeanUtils.copyProperties(request, note);
        note.setUserId(userId);
        note.setLikeCount(0);
        note.setCreateTime(LocalDateTime.now());
        note.setUpdateTime(LocalDateTime.now());
        
        int result = noteMapper.insert(note);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "笔记创建失败");
        }
        
        log.info("学习笔记创建成功: noteId={}", note.getId());
        return note;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LearningNote updateNote(Long userId, Long noteId, LearningNoteRequest request) {
        log.info("更新学习笔记: userId={}, noteId={}", userId, noteId);
        
        LearningNote existingNote = noteMapper.selectById(noteId);
        if (existingNote == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "笔记不存在");
        }
        
        if (!existingNote.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此笔记");
        }
        
        BeanUtils.copyProperties(request, existingNote);
        existingNote.setUpdateTime(LocalDateTime.now());
        
        int result = noteMapper.updateById(existingNote);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "笔记更新失败");
        }
        
        log.info("学习笔记更新成功: noteId={}", noteId);
        return existingNote;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNote(Long userId, Long noteId) {
        log.info("删除学习笔记: userId={}, noteId={}", userId, noteId);
        
        LearningNote note = noteMapper.selectById(noteId);
        if (note == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "笔记不存在");
        }
        
        if (!note.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此笔记");
        }
        
        int result = noteMapper.deleteById(noteId);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "笔记删除失败");
        }
        
        log.info("学习笔记删除成功: noteId={}", noteId);
        return true;
    }
    
    @Override
    public LearningNote getNoteById(Long noteId) {
        return noteMapper.selectById(noteId);
    }
    
    @Override
    public IPage<LearningNote> getUserNotesPage(Long userId, Integer page, Integer size) {
        Page<LearningNote> pageParam = new Page<>(page, size);
        return noteMapper.selectUserNotesPage(pageParam, userId);
    }
    
    @Override
    public List<LearningNote> getCourseNotes(Long userId, Long courseId) {
        return noteMapper.selectByCourse(userId, courseId);
    }
    
    @Override
    public List<LearningNote> getLessonNotes(Long userId, Long lessonId) {
        return noteMapper.selectByLesson(userId, lessonId);
    }
    
    @Override
    public IPage<LearningNote> searchNotes(Long userId, String keyword, Integer page, Integer size) {
        Page<LearningNote> pageParam = new Page<>(page, size);
        return noteMapper.searchNotes(pageParam, userId, keyword);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseFavorite favoriteCourse(Long userId, Long courseId, String category, String remark) {
        log.info("收藏课程: userId={}, courseId={}", userId, courseId);
        
        // 检查是否已收藏
        CourseFavorite existing = favoriteMapper.selectByUserAndCourse(userId, courseId);
        if (existing != null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "课程已收藏");
        }
        
        CourseFavorite favorite = new CourseFavorite();
        favorite.setUserId(userId);
        favorite.setCourseId(courseId);
        favorite.setCategory(category);
        favorite.setRemark(remark);
        favorite.setCreateTime(LocalDateTime.now());
        favorite.setUpdateTime(LocalDateTime.now());
        
        int result = favoriteMapper.insert(favorite);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "收藏失败");
        }
        
        log.info("课程收藏成功: favoriteId={}", favorite.getId());
        return favorite;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unfavoriteCourse(Long userId, Long courseId) {
        log.info("取消收藏: userId={}, courseId={}", userId, courseId);
        
        CourseFavorite favorite = favoriteMapper.selectByUserAndCourse(userId, courseId);
        if (favorite == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "收藏记录不存在");
        }
        
        int result = favoriteMapper.deleteById(favorite.getId());
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "取消收藏失败");
        }
        
        log.info("取消收藏成功: favoriteId={}", favorite.getId());
        return true;
    }
    
    @Override
    public boolean isFavorited(Long userId, Long courseId) {
        CourseFavorite favorite = favoriteMapper.selectByUserAndCourse(userId, courseId);
        return favorite != null;
    }
    
    @Override
    public IPage<CourseFavorite> getFavoritesPage(Long userId, Integer page, Integer size) {
        Page<CourseFavorite> pageParam = new Page<>(page, size);
        return favoriteMapper.selectUserFavoritesPage(pageParam, userId);
    }
    
    @Override
    public List<CourseFavorite> getFavoritesByCategory(Long userId, String category) {
        return favoriteMapper.selectByCategory(userId, category);
    }
    
    @Override
    public List<String> getFavoriteCategories(Long userId) {
        return favoriteMapper.selectCategories(userId);
    }
    
    @Override
    public LearningStatsVO getLearningStats(Long userId) {
        return statsMapper.selectUserStats(userId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDailyStats(Long userId) {
        try {
            LocalDate today = LocalDate.now();
            
            // 统计今日学习数据
            Integer todayDuration = progressMapper.sumLearningDuration(userId);
            Integer completedLessons = progressMapper.countCompletedLessons(userId, null);
            Integer learnCourses = progressMapper.countLearnedCourses(userId);
            Integer newNotes = noteMapper.countUserNotes(userId);
            
            // 计算积分
            Integer points = calculatePoints(todayDuration, completedLessons, newNotes);
            
            // 更新统计
            int result = statsMapper.updateDailyStats(userId, today, todayDuration, 
                    completedLessons, learnCourses, newNotes, points);
            
            return result > 0;
        } catch (Exception e) {
            log.error("更新每日统计失败: userId={}", userId, e);
            return false;
        }
    }
    
    @Override
    public boolean likeNote(Long noteId) {
        try {
            int result = noteMapper.incrementLikeCount(noteId);
            return result > 0;
        } catch (Exception e) {
            log.error("点赞笔记失败: noteId={}", noteId, e);
            return false;
        }
    }
    
    @Override
    public boolean unlikeNote(Long noteId) {
        try {
            int result = noteMapper.decrementLikeCount(noteId);
            return result > 0;
        } catch (Exception e) {
            log.error("取消点赞失败: noteId={}", noteId, e);
            return false;
        }
    }
    
    /**
     * 计算学习积分
     */
    private Integer calculatePoints(Integer duration, Integer lessons, Integer notes) {
        int points = 0;
        
        // 学习时长积分：每分钟1分
        if (duration != null) {
            points += duration;
        }
        
        // 完成课时积分：每课时10分
        if (lessons != null) {
            points += lessons * 10;
        }
        
        // 笔记积分：每条笔记5分
        if (notes != null) {
            points += notes * 5;
        }
        
        return points;
    }
}
