package com.smartlearning.learning.controller;

import com.smartlearning.common.result.Result;
import com.smartlearning.learning.document.LearningBehaviorDocument;
import com.smartlearning.learning.document.LearningNoteDocument;
import com.smartlearning.learning.document.UserLearningStatsDocument;
import com.smartlearning.learning.service.MongoLearningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB学习功能控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/mongo/learning")
@RequiredArgsConstructor
@Tag(name = "MongoDB学习功能", description = "基于MongoDB的学习功能API")
public class MongoLearningController {
    
    private final MongoLearningService mongoLearningService;
    
    // ==================== 学习笔记相关 ====================
    
    @PostMapping("/notes")
    @Operation(summary = "保存学习笔记")
    public Result<LearningNoteDocument> saveLearningNote(@RequestBody LearningNoteDocument note) {
        try {
            LearningNoteDocument saved = mongoLearningService.saveLearningNote(note);
            return Result.success(saved);
        } catch (Exception e) {
            log.error("保存学习笔记失败", e);
            return Result.error("保存学习笔记失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/notes/{id}")
    @Operation(summary = "根据ID查询笔记")
    public Result<LearningNoteDocument> getNoteById(@PathVariable String id) {
        Optional<LearningNoteDocument> note = mongoLearningService.findNoteById(id);
        return note.map(Result::success).orElse(Result.error("笔记不存在"));
    }
    
    @GetMapping("/notes/user/{userId}")
    @Operation(summary = "分页查询用户笔记")
    public Result<Page<LearningNoteDocument>> getUserNotes(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LearningNoteDocument> notes = mongoLearningService.findUserNotes(userId, pageable);
        return Result.success(notes);
    }
    
    @GetMapping("/notes/course/{courseId}")
    @Operation(summary = "查询课程笔记")
    public Result<List<LearningNoteDocument>> getCourseNotes(
            @PathVariable Long courseId,
            @RequestParam Long userId) {
        List<LearningNoteDocument> notes = mongoLearningService.findCourseNotes(userId, courseId);
        return Result.success(notes);
    }
    
    @GetMapping("/notes/public/{courseId}")
    @Operation(summary = "查询公开笔记")
    public Result<Page<LearningNoteDocument>> getPublicNotes(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LearningNoteDocument> notes = mongoLearningService.findPublicNotes(courseId, pageable);
        return Result.success(notes);
    }
    
    @GetMapping("/notes/search")
    @Operation(summary = "搜索笔记")
    public Result<Page<LearningNoteDocument>> searchNotes(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LearningNoteDocument> notes = mongoLearningService.searchNotes(keyword, pageable);
        return Result.success(notes);
    }
    
    @PostMapping("/notes/{id}/like")
    @Operation(summary = "点赞笔记")
    public Result<Boolean> likeNote(@PathVariable String id) {
        boolean success = mongoLearningService.likeNote(id);
        return success ? Result.success(true) : Result.error("点赞失败");
    }
    
    @DeleteMapping("/notes/{id}")
    @Operation(summary = "删除笔记")
    public Result<Boolean> deleteNote(@PathVariable String id, @RequestParam Long userId) {
        boolean success = mongoLearningService.deleteNote(id, userId);
        return success ? Result.success(true) : Result.error("删除失败");
    }
    
    // ==================== 学习行为相关 ====================
    
    @PostMapping("/behaviors")
    @Operation(summary = "记录学习行为")
    public Result<LearningBehaviorDocument> recordBehavior(@RequestBody LearningBehaviorDocument behavior) {
        try {
            LearningBehaviorDocument saved = mongoLearningService.recordLearningBehavior(behavior);
            return Result.success(saved);
        } catch (Exception e) {
            log.error("记录学习行为失败", e);
            return Result.error("记录学习行为失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/behaviors/user/{userId}")
    @Operation(summary = "查询用户学习行为")
    public Result<Page<LearningBehaviorDocument>> getUserBehaviors(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LearningBehaviorDocument> behaviors = mongoLearningService.findUserBehaviors(userId, pageable);
        return Result.success(behaviors);
    }
    
    @GetMapping("/behaviors/duration/{userId}")
    @Operation(summary = "统计学习时长")
    public Result<Integer> getLearningDuration(
            @PathVariable Long userId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        LocalDateTime start = LocalDateTime.parse(startTime);
        LocalDateTime end = LocalDateTime.parse(endTime);
        Integer duration = mongoLearningService.calculateLearningDuration(userId, start, end);
        return Result.success(duration);
    }
    
    // ==================== 学习统计相关 ====================
    
    @PostMapping("/stats")
    @Operation(summary = "保存学习统计")
    public Result<UserLearningStatsDocument> saveLearningStats(@RequestBody UserLearningStatsDocument stats) {
        try {
            UserLearningStatsDocument saved = mongoLearningService.saveLearningStats(stats);
            return Result.success(saved);
        } catch (Exception e) {
            log.error("保存学习统计失败", e);
            return Result.error("保存学习统计失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/stats/{userId}")
    @Operation(summary = "查询用户统计数据")
    public Result<UserLearningStatsDocument> getUserStats(
            @PathVariable Long userId,
            @RequestParam String date) {
        LocalDate statDate = LocalDate.parse(date);
        Optional<UserLearningStatsDocument> stats = mongoLearningService.findUserStats(userId, statDate);
        return stats.map(Result::success).orElse(Result.error("统计数据不存在"));
    }
    
    @GetMapping("/stats/recent/{userId}")
    @Operation(summary = "查询最近统计数据")
    public Result<List<UserLearningStatsDocument>> getRecentStats(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days) {
        List<UserLearningStatsDocument> stats = mongoLearningService.findRecentStats(userId, days);
        return Result.success(stats);
    }
    
    @GetMapping("/stats/ranking")
    @Operation(summary = "查询学习排行榜")
    public Result<Page<UserLearningStatsDocument>> getLearningRanking(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserLearningStatsDocument> ranking = mongoLearningService.findLearningRanking(start, end, pageable);
        return Result.success(ranking);
    }
    
    @PostMapping("/stats/generate/{userId}")
    @Operation(summary = "生成每日统计")
    public Result<UserLearningStatsDocument> generateDailyStats(
            @PathVariable Long userId,
            @RequestParam String date) {
        LocalDate statDate = LocalDate.parse(date);
        UserLearningStatsDocument stats = mongoLearningService.generateDailyStats(userId, statDate);
        return Result.success(stats);
    }
    
    @PutMapping("/stats/update/{userId}")
    @Operation(summary = "更新学习统计")
    public Result<Boolean> updateLearningStats(
            @PathVariable Long userId,
            @RequestParam String date,
            @RequestParam Integer duration,
            @RequestParam(required = false) Integer lessons) {
        LocalDate statDate = LocalDate.parse(date);
        boolean success = mongoLearningService.updateLearningStats(userId, statDate, duration, lessons);
        return success ? Result.success(true) : Result.error("更新失败");
    }
}
