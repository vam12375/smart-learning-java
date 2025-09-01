package com.smartlearning.learning.service.impl;

import com.smartlearning.learning.document.LearningBehaviorDocument;
import com.smartlearning.learning.document.LearningNoteDocument;
import com.smartlearning.learning.document.UserLearningStatsDocument;
import com.smartlearning.learning.repository.LearningBehaviorRepository;
import com.smartlearning.learning.repository.LearningNoteRepository;
import com.smartlearning.learning.repository.UserLearningStatsRepository;
import com.smartlearning.learning.service.MongoLearningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB学习服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MongoLearningServiceImpl implements MongoLearningService {
    
    private final LearningNoteRepository noteRepository;
    private final LearningBehaviorRepository behaviorRepository;
    private final UserLearningStatsRepository statsRepository;
    
    // ==================== 学习笔记相关 ====================
    
    @Override
    public LearningNoteDocument saveLearningNote(LearningNoteDocument note) {
        try {
            if (note.getCreateTime() == null) {
                note.setCreateTime(LocalDateTime.now());
            }
            note.setUpdateTime(LocalDateTime.now());
            if (note.getDeleted() == null) {
                note.setDeleted(false);
            }
            if (note.getLikeCount() == null) {
                note.setLikeCount(0);
            }
            
            LearningNoteDocument saved = noteRepository.save(note);
            log.info("保存学习笔记成功: id={}, userId={}, title={}", 
                    saved.getId(), saved.getUserId(), saved.getTitle());
            return saved;
        } catch (Exception e) {
            log.error("保存学习笔记失败", e);
            throw new RuntimeException("保存学习笔记失败", e);
        }
    }
    
    @Override
    public Optional<LearningNoteDocument> findNoteById(String id) {
        return noteRepository.findById(id);
    }
    
    @Override
    public Page<LearningNoteDocument> findUserNotes(Long userId, Pageable pageable) {
        return noteRepository.findByUserIdAndDeletedFalseOrderByCreateTimeDesc(userId, pageable);
    }
    
    @Override
    public List<LearningNoteDocument> findCourseNotes(Long userId, Long courseId) {
        return noteRepository.findByUserIdAndCourseIdAndDeletedFalseOrderByCreateTimeDesc(userId, courseId);
    }
    
    @Override
    public Page<LearningNoteDocument> findPublicNotes(Long courseId, Pageable pageable) {
        return noteRepository.findByCourseIdAndIsPublicTrueAndDeletedFalseOrderByLikeCountDescCreateTimeDesc(courseId, pageable);
    }
    
    @Override
    public Page<LearningNoteDocument> searchNotes(String keyword, Pageable pageable) {
        return noteRepository.searchNotes(keyword, pageable);
    }
    
    @Override
    public boolean likeNote(String noteId) {
        try {
            Optional<LearningNoteDocument> noteOpt = noteRepository.findById(noteId);
            if (noteOpt.isPresent()) {
                LearningNoteDocument note = noteOpt.get();
                note.setLikeCount(note.getLikeCount() + 1);
                note.setUpdateTime(LocalDateTime.now());
                noteRepository.save(note);
                log.info("笔记点赞成功: noteId={}, likeCount={}", noteId, note.getLikeCount());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("笔记点赞失败: noteId={}", noteId, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteNote(String noteId, Long userId) {
        try {
            Optional<LearningNoteDocument> noteOpt = noteRepository.findById(noteId);
            if (noteOpt.isPresent()) {
                LearningNoteDocument note = noteOpt.get();
                if (!note.getUserId().equals(userId)) {
                    log.warn("用户无权删除笔记: noteId={}, userId={}, noteUserId={}", 
                            noteId, userId, note.getUserId());
                    return false;
                }
                note.setDeleted(true);
                note.setUpdateTime(LocalDateTime.now());
                noteRepository.save(note);
                log.info("删除笔记成功: noteId={}, userId={}", noteId, userId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("删除笔记失败: noteId={}, userId={}", noteId, userId, e);
            return false;
        }
    }
    
    // ==================== 学习行为相关 ====================
    
    @Override
    public LearningBehaviorDocument recordLearningBehavior(LearningBehaviorDocument behavior) {
        try {
            if (behavior.getActionTime() == null) {
                behavior.setActionTime(LocalDateTime.now());
            }
            behavior.setCreateTime(LocalDateTime.now());
            
            LearningBehaviorDocument saved = behaviorRepository.save(behavior);
            log.info("记录学习行为成功: userId={}, courseId={}, actionType={}, duration={}", 
                    saved.getUserId(), saved.getCourseId(), saved.getActionType(), saved.getDuration());
            return saved;
        } catch (Exception e) {
            log.error("记录学习行为失败", e);
            throw new RuntimeException("记录学习行为失败", e);
        }
    }
    
    @Override
    public Page<LearningBehaviorDocument> findUserBehaviors(Long userId, Pageable pageable) {
        return behaviorRepository.findByUserIdOrderByActionTimeDesc(userId, pageable);
    }
    
    @Override
    public List<LearningBehaviorDocument> findBehaviorsByTimeRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return behaviorRepository.findByUserIdAndActionTimeBetweenOrderByActionTimeDesc(userId, startTime, endTime);
    }
    
    @Override
    public Integer calculateLearningDuration(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        List<LearningBehaviorDocument> behaviors = behaviorRepository.findUserLearningDuration(userId, startTime, endTime);
        return behaviors.stream()
                .mapToInt(behavior -> behavior.getDuration() != null ? behavior.getDuration() : 0)
                .sum();
    }
    
    @Override
    public List<LearningBehaviorDocument> analyzeLearningHabits(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.atTime(23, 59, 59);
        return behaviorRepository.findByUserIdAndActionTimeBetweenOrderByActionTimeDesc(userId, startTime, endTime);
    }
    
    // ==================== 学习统计相关 ====================
    
    @Override
    public UserLearningStatsDocument saveLearningStats(UserLearningStatsDocument stats) {
        try {
            if (stats.getCreateTime() == null) {
                stats.setCreateTime(LocalDateTime.now());
            }
            stats.setUpdateTime(LocalDateTime.now());
            
            UserLearningStatsDocument saved = statsRepository.save(stats);
            log.info("保存学习统计成功: userId={}, statDate={}, totalLearningTime={}", 
                    saved.getUserId(), saved.getStatDate(), saved.getTotalLearningTime());
            return saved;
        } catch (Exception e) {
            log.error("保存学习统计失败", e);
            throw new RuntimeException("保存学习统计失败", e);
        }
    }
    
    @Override
    public Optional<UserLearningStatsDocument> findUserStats(Long userId, LocalDate date) {
        return statsRepository.findByUserIdAndStatDate(userId, date);
    }
    
    @Override
    public List<UserLearningStatsDocument> findRecentStats(Long userId, int days) {
        LocalDate startDate = LocalDate.now().minusDays(days - 1);
        return statsRepository.findRecentStats(userId, startDate);
    }
    
    @Override
    public Page<UserLearningStatsDocument> findLearningRanking(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return statsRepository.findLearningRanking(startDate, endDate, pageable);
    }
    
    @Override
    public UserLearningStatsDocument generateDailyStats(Long userId, LocalDate date) {
        // 这里可以根据学习行为数据生成统计
        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.atTime(23, 59, 59);
        
        List<LearningBehaviorDocument> behaviors = findBehaviorsByTimeRange(userId, startTime, endTime);
        
        UserLearningStatsDocument stats = new UserLearningStatsDocument();
        stats.setUserId(userId);
        stats.setStatDate(date);
        stats.setLearningDays(behaviors.isEmpty() ? 0 : 1);
        
        // 计算总学习时长（转换为分钟）
        int totalSeconds = behaviors.stream()
                .mapToInt(behavior -> behavior.getDuration() != null ? behavior.getDuration() : 0)
                .sum();
        stats.setTotalLearningTime(totalSeconds / 60);
        
        // 统计各种行为数量
        stats.setVideosWatched((int) behaviors.stream().filter(b -> b.getActionType() == 1).count());
        stats.setExercisesCompleted((int) behaviors.stream().filter(b -> b.getActionType() == 2).count());
        stats.setDiscussionsParticipated((int) behaviors.stream().filter(b -> b.getActionType() == 3).count());
        stats.setMaterialsDownloaded((int) behaviors.stream().filter(b -> b.getActionType() == 4).count());
        
        return saveLearningStats(stats);
    }
    
    @Override
    public boolean updateLearningStats(Long userId, LocalDate date, Integer duration, Integer lessons) {
        try {
            Optional<UserLearningStatsDocument> statsOpt = findUserStats(userId, date);
            UserLearningStatsDocument stats;
            
            if (statsOpt.isPresent()) {
                stats = statsOpt.get();
                stats.setTotalLearningTime(stats.getTotalLearningTime() + duration);
                if (lessons != null) {
                    stats.setCoursesLearned(stats.getCoursesLearned() + lessons);
                }
            } else {
                stats = new UserLearningStatsDocument();
                stats.setUserId(userId);
                stats.setStatDate(date);
                stats.setTotalLearningTime(duration);
                stats.setCoursesLearned(lessons != null ? lessons : 0);
                stats.setLearningDays(1);
            }
            
            saveLearningStats(stats);
            return true;
        } catch (Exception e) {
            log.error("更新学习统计失败: userId={}, date={}", userId, date, e);
            return false;
        }
    }
}
