package com.smartlearning.learning.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartlearning.common.result.Result;
import com.smartlearning.learning.dto.LearningNoteRequest;
import com.smartlearning.learning.dto.LearningProgressRequest;
import com.smartlearning.learning.entity.CourseFavorite;
import com.smartlearning.learning.entity.LearningNote;
import com.smartlearning.learning.entity.LearningProgress;
import com.smartlearning.learning.service.LearningService;
import com.smartlearning.learning.vo.LearningProgressVO;
import com.smartlearning.learning.vo.LearningStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学习控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
@Tag(name = "学习管理", description = "学习进度、笔记、收藏等相关接口")
public class LearningController {
    
    private final LearningService learningService;
    
    /**
     * 更新学习进度
     */
    @PostMapping("/progress")
    @Operation(summary = "更新学习进度", description = "更新用户的学习进度")
    public Result<LearningProgress> updateProgress(@Valid @RequestBody LearningProgressRequest request,
                                                 @RequestHeader("X-User-Id") Long userId) {
        log.info("更新学习进度请求: userId={}, lessonId={}", userId, request.getLessonId());
        LearningProgress progress = learningService.updateProgress(userId, request);
        return Result.success("进度更新成功", progress);
    }
    
    /**
     * 获取学习进度
     */
    @GetMapping("/progress/lesson/{lessonId}")
    @Operation(summary = "获取学习进度", description = "获取用户对指定课时的学习进度")
    public Result<LearningProgress> getUserProgress(@Parameter(description = "课时ID") @PathVariable Long lessonId,
                                                  @RequestHeader("X-User-Id") Long userId) {
        log.info("获取学习进度请求: userId={}, lessonId={}", userId, lessonId);
        LearningProgress progress = learningService.getUserProgress(userId, lessonId);
        return Result.success("获取成功", progress);
    }
    
    /**
     * 获取课程学习进度
     */
    @GetMapping("/progress/course/{courseId}")
    @Operation(summary = "获取课程学习进度", description = "获取用户对指定课程的学习进度")
    public Result<List<LearningProgressVO>> getCourseProgress(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                                            @RequestHeader("X-User-Id") Long userId) {
        log.info("获取课程学习进度请求: userId={}, courseId={}", userId, courseId);
        List<LearningProgressVO> progress = learningService.getCourseProgress(userId, courseId);
        return Result.success("获取成功", progress);
    }
    
    /**
     * 分页查询学习进度
     */
    @GetMapping("/progress")
    @Operation(summary = "分页查询学习进度", description = "分页查询用户的学习进度")
    public Result<IPage<LearningProgressVO>> getUserProgressPage(@RequestHeader("X-User-Id") Long userId,
                                                               @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                                               @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size) {
        log.info("分页查询学习进度请求: userId={}, page={}, size={}", userId, page, size);
        IPage<LearningProgressVO> result = learningService.getUserProgressPage(userId, page, size);
        return Result.success("查询成功", result);
    }
    
    /**
     * 获取最近学习
     */
    @GetMapping("/progress/recent")
    @Operation(summary = "获取最近学习", description = "获取用户最近的学习记录")
    public Result<List<LearningProgressVO>> getRecentLearning(@RequestHeader("X-User-Id") Long userId,
                                                            @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取最近学习请求: userId={}, limit={}", userId, limit);
        List<LearningProgressVO> recent = learningService.getRecentLearning(userId, limit);
        return Result.success("获取成功", recent);
    }
    
    /**
     * 创建学习笔记
     */
    @PostMapping("/notes")
    @Operation(summary = "创建学习笔记", description = "创建新的学习笔记")
    public Result<LearningNote> createNote(@Valid @RequestBody LearningNoteRequest request,
                                         @RequestHeader("X-User-Id") Long userId) {
        log.info("创建学习笔记请求: userId={}, lessonId={}", userId, request.getLessonId());
        LearningNote note = learningService.createNote(userId, request);
        return Result.success("笔记创建成功", note);
    }
    
    /**
     * 更新学习笔记
     */
    @PutMapping("/notes/{noteId}")
    @Operation(summary = "更新学习笔记", description = "更新指定的学习笔记")
    public Result<LearningNote> updateNote(@Parameter(description = "笔记ID") @PathVariable Long noteId,
                                         @Valid @RequestBody LearningNoteRequest request,
                                         @RequestHeader("X-User-Id") Long userId) {
        log.info("更新学习笔记请求: userId={}, noteId={}", userId, noteId);
        LearningNote note = learningService.updateNote(userId, noteId, request);
        return Result.success("笔记更新成功", note);
    }
    
    /**
     * 删除学习笔记
     */
    @DeleteMapping("/notes/{noteId}")
    @Operation(summary = "删除学习笔记", description = "删除指定的学习笔记")
    public Result<Void> deleteNote(@Parameter(description = "笔记ID") @PathVariable Long noteId,
                                 @RequestHeader("X-User-Id") Long userId) {
        log.info("删除学习笔记请求: userId={}, noteId={}", userId, noteId);
        learningService.deleteNote(userId, noteId);
        return Result.success("笔记删除成功");
    }
    
    /**
     * 获取笔记详情
     */
    @GetMapping("/notes/{noteId}")
    @Operation(summary = "获取笔记详情", description = "获取指定笔记的详细信息")
    public Result<LearningNote> getNoteById(@Parameter(description = "笔记ID") @PathVariable Long noteId) {
        log.info("获取笔记详情请求: noteId={}", noteId);
        LearningNote note = learningService.getNoteById(noteId);
        return Result.success("获取成功", note);
    }
    
    /**
     * 分页查询用户笔记
     */
    @GetMapping("/notes")
    @Operation(summary = "分页查询用户笔记", description = "分页查询用户的学习笔记")
    public Result<IPage<LearningNote>> getUserNotesPage(@RequestHeader("X-User-Id") Long userId,
                                                       @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                                       @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size) {
        log.info("分页查询用户笔记请求: userId={}, page={}, size={}", userId, page, size);
        IPage<LearningNote> result = learningService.getUserNotesPage(userId, page, size);
        return Result.success("查询成功", result);
    }
    
    /**
     * 获取课程笔记
     */
    @GetMapping("/notes/course/{courseId}")
    @Operation(summary = "获取课程笔记", description = "获取用户在指定课程中的笔记")
    public Result<List<LearningNote>> getCourseNotes(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                                    @RequestHeader("X-User-Id") Long userId) {
        log.info("获取课程笔记请求: userId={}, courseId={}", userId, courseId);
        List<LearningNote> notes = learningService.getCourseNotes(userId, courseId);
        return Result.success("获取成功", notes);
    }
    
    /**
     * 获取课时笔记
     */
    @GetMapping("/notes/lesson/{lessonId}")
    @Operation(summary = "获取课时笔记", description = "获取用户在指定课时中的笔记")
    public Result<List<LearningNote>> getLessonNotes(@Parameter(description = "课时ID") @PathVariable Long lessonId,
                                                    @RequestHeader("X-User-Id") Long userId) {
        log.info("获取课时笔记请求: userId={}, lessonId={}", userId, lessonId);
        List<LearningNote> notes = learningService.getLessonNotes(userId, lessonId);
        return Result.success("获取成功", notes);
    }
    
    /**
     * 搜索笔记
     */
    @GetMapping("/notes/search")
    @Operation(summary = "搜索笔记", description = "根据关键词搜索用户的笔记")
    public Result<IPage<LearningNote>> searchNotes(@RequestHeader("X-User-Id") Long userId,
                                                  @Parameter(description = "搜索关键词") @RequestParam String keyword,
                                                  @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                                  @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size) {
        log.info("搜索笔记请求: userId={}, keyword={}", userId, keyword);
        IPage<LearningNote> result = learningService.searchNotes(userId, keyword, page, size);
        return Result.success("搜索成功", result);
    }
    
    /**
     * 收藏课程
     */
    @PostMapping("/favorites/{courseId}")
    @Operation(summary = "收藏课程", description = "收藏指定的课程")
    public Result<CourseFavorite> favoriteCourse(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                               @RequestHeader("X-User-Id") Long userId,
                                               @Parameter(description = "收藏分组") @RequestParam(required = false) String category,
                                               @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        log.info("收藏课程请求: userId={}, courseId={}", userId, courseId);
        CourseFavorite favorite = learningService.favoriteCourse(userId, courseId, category, remark);
        return Result.success("收藏成功", favorite);
    }
    
    /**
     * 取消收藏
     */
    @DeleteMapping("/favorites/{courseId}")
    @Operation(summary = "取消收藏", description = "取消收藏指定的课程")
    public Result<Void> unfavoriteCourse(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                       @RequestHeader("X-User-Id") Long userId) {
        log.info("取消收藏请求: userId={}, courseId={}", userId, courseId);
        learningService.unfavoriteCourse(userId, courseId);
        return Result.success("取消收藏成功");
    }
    
    /**
     * 检查是否已收藏
     */
    @GetMapping("/favorites/{courseId}/check")
    @Operation(summary = "检查是否已收藏", description = "检查用户是否已收藏指定课程")
    public Result<Boolean> isFavorited(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                     @RequestHeader("X-User-Id") Long userId) {
        log.info("检查收藏状态请求: userId={}, courseId={}", userId, courseId);
        boolean favorited = learningService.isFavorited(userId, courseId);
        return Result.success("检查完成", favorited);
    }
    
    /**
     * 分页查询收藏列表
     */
    @GetMapping("/favorites")
    @Operation(summary = "分页查询收藏列表", description = "分页查询用户的收藏列表")
    public Result<IPage<CourseFavorite>> getFavoritesPage(@RequestHeader("X-User-Id") Long userId,
                                                        @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                                        @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size) {
        log.info("分页查询收藏列表请求: userId={}, page={}, size={}", userId, page, size);
        IPage<CourseFavorite> result = learningService.getFavoritesPage(userId, page, size);
        return Result.success("查询成功", result);
    }
    
    /**
     * 获取学习统计
     */
    @GetMapping("/stats")
    @Operation(summary = "获取学习统计", description = "获取用户的学习统计数据")
    public Result<LearningStatsVO> getLearningStats(@RequestHeader("X-User-Id") Long userId) {
        log.info("获取学习统计请求: userId={}", userId);
        LearningStatsVO stats = learningService.getLearningStats(userId);
        return Result.success("获取成功", stats);
    }
    
    /**
     * 点赞笔记
     */
    @PostMapping("/notes/{noteId}/like")
    @Operation(summary = "点赞笔记", description = "为指定笔记点赞")
    public Result<Void> likeNote(@Parameter(description = "笔记ID") @PathVariable Long noteId) {
        log.info("点赞笔记请求: noteId={}", noteId);
        learningService.likeNote(noteId);
        return Result.success("点赞成功");
    }
    
    /**
     * 取消点赞
     */
    @DeleteMapping("/notes/{noteId}/like")
    @Operation(summary = "取消点赞", description = "取消对指定笔记的点赞")
    public Result<Void> unlikeNote(@Parameter(description = "笔记ID") @PathVariable Long noteId) {
        log.info("取消点赞请求: noteId={}", noteId);
        learningService.unlikeNote(noteId);
        return Result.success("取消点赞成功");
    }
}
