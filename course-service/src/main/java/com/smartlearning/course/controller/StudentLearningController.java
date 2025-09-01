package com.smartlearning.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartlearning.common.result.Result;
import com.smartlearning.course.service.CourseService;
import com.smartlearning.course.vo.CourseVO;
import com.smartlearning.course.dto.CourseQueryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生学习控制器
 * 处理学生个人学习相关的课程接口
 */
@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "学生学习管理", description = "学生个人学习相关接口")
public class StudentLearningController {

    private final CourseService courseService;

    /**
     * 获取我的课程
     */
    @GetMapping("/my")
    @Operation(summary = "获取我的课程", description = "获取当前用户的课程列表")
    public Result<IPage<CourseVO>> getMyCourses(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "12") Integer size,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "分类") @RequestParam(required = false) String category,
            @Parameter(description = "排序") @RequestParam(defaultValue = "recent") String sort) {

        log.info("获取我的课程请求: userId={}, page={}, size={}, keyword={}, status={}, category={}, sort={}",
                userId, page, size, keyword, status, category, sort);

        // 构建查询条件
        CourseQueryRequest query = new CourseQueryRequest();
        query.setPage(page);
        query.setSize(size);
        query.setKeyword(keyword);
        query.setStatus(status);

        // 这里应该调用学习服务来获取用户的课程，暂时返回空结果
        // 在实际实现中，需要通过Feign调用learning-service来获取用户的学习记录
        IPage<CourseVO> result = courseService.getCoursePage(query);

        // 临时返回空结果，避免403错误
        result.setRecords(new ArrayList<>());
        result.setTotal(0);

        return Result.success("查询成功", result);
    }

    /**
     * 获取课程学习进度
     */
    @GetMapping("/{courseId}/progress")
    @Operation(summary = "获取课程学习进度", description = "获取指定课程的学习进度")
    public Result<Object> getCourseProgress(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "课程ID") @PathVariable Long courseId) {

        log.info("获取课程学习进度请求: userId={}, courseId={}", userId, courseId);

        // 这里应该调用学习服务来获取学习进度
        // 暂时返回模拟数据
        Object progress = new Object() {
            public final int completedLessons = 0;
            public final int totalLessons = 1;
            public final double progressPercentage = 0.0;
            public final long studyTime = 0;
        };

        return Result.success("查询成功", progress);
    }

    /**
     * 课程报名
     */
    @PostMapping("/enroll")
    @Operation(summary = "课程报名", description = "报名参加课程")
    public Result<Object> enrollCourse(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Object enrollRequest) {

        log.info("课程报名请求: userId={}, request={}", userId, enrollRequest);

        // 这里应该调用学习服务来处理课程报名
        // 暂时返回成功结果
        return Result.success("报名成功", null);
    }

    /**
     * 收藏课程
     */
    @PostMapping("/{courseId}/favorite")
    @Operation(summary = "收藏课程", description = "收藏指定课程")
    public Result<Object> favoriteCourse(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "课程ID") @PathVariable Long courseId) {

        log.info("收藏课程请求: userId={}, courseId={}", userId, courseId);

        // 这里应该调用学习服务来处理课程收藏
        // 暂时返回成功结果
        return Result.success("收藏成功", null);
    }

    /**
     * 取消收藏课程
     */
    @DeleteMapping("/{courseId}/favorite")
    @Operation(summary = "取消收藏课程", description = "取消收藏指定课程")
    public Result<Object> unfavoriteCourse(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "课程ID") @PathVariable Long courseId) {

        log.info("取消收藏课程请求: userId={}, courseId={}", userId, courseId);

        // 这里应该调用学习服务来处理取消收藏
        // 暂时返回成功结果
        return Result.success("取消收藏成功", null);
    }

    /**
     * 获取收藏的课程
     */
    @GetMapping("/favorites")
    @Operation(summary = "获取收藏的课程", description = "获取用户收藏的课程列表")
    public Result<IPage<CourseVO>> getFavoriteCourses(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "12") Integer size) {

        log.info("获取收藏课程请求: userId={}, page={}, size={}", userId, page, size);

        // 构建查询条件
        CourseQueryRequest query = new CourseQueryRequest();
        query.setPage(page);
        query.setSize(size);

        // 这里应该调用学习服务来获取收藏的课程
        // 暂时返回空结果
        IPage<CourseVO> result = courseService.getCoursePage(query);
        result.setRecords(new ArrayList<>());
        result.setTotal(0);

        return Result.success("查询成功", result);
    }
}
