package com.smartlearning.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartlearning.common.result.Result;
import com.smartlearning.course.dto.CourseCreateRequest;
import com.smartlearning.course.dto.CourseQueryRequest;
import com.smartlearning.course.entity.Course;
import com.smartlearning.course.service.CourseService;
import com.smartlearning.course.vo.CourseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
@Tag(name = "课程管理", description = "课程CRUD、发布审核等相关接口")
public class CourseController {
    
    private final CourseService courseService;
    
    /**
     * 创建课程
     */
    @PostMapping
    @Operation(summary = "创建课程", description = "教师创建新课程")
    public Result<Course> createCourse(@Valid @RequestBody CourseCreateRequest request,
                                     @RequestHeader("X-User-Id") Long teacherId) {
        log.info("创建课程请求: title={}, teacherId={}", request.getTitle(), teacherId);
        Course course = courseService.createCourse(request, teacherId);
        return Result.success("课程创建成功", course);
    }
    
    /**
     * 更新课程
     */
    @PutMapping("/{courseId}")
    @Operation(summary = "更新课程", description = "教师更新课程信息")
    public Result<Course> updateCourse(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                     @Valid @RequestBody CourseCreateRequest request,
                                     @RequestHeader("X-User-Id") Long teacherId) {
        log.info("更新课程请求: courseId={}, teacherId={}", courseId, teacherId);
        Course course = courseService.updateCourse(courseId, request, teacherId);
        return Result.success("课程更新成功", course);
    }
    
    /**
     * 删除课程
     */
    @DeleteMapping("/{courseId}")
    @Operation(summary = "删除课程", description = "教师删除课程")
    public Result<Void> deleteCourse(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                   @RequestHeader("X-User-Id") Long teacherId) {
        log.info("删除课程请求: courseId={}, teacherId={}", courseId, teacherId);
        courseService.deleteCourse(courseId, teacherId);
        return Result.success("课程删除成功");
    }
    
    /**
     * 获取课程详情
     */
    @GetMapping("/{courseId}")
    @Operation(summary = "获取课程详情", description = "根据ID获取课程详细信息")
    public Result<CourseVO> getCourseDetail(@Parameter(description = "课程ID") @PathVariable Long courseId) {
        log.info("获取课程详情请求: courseId={}", courseId);
        CourseVO courseVO = courseService.getCourseDetail(courseId);
        return Result.success("获取成功", courseVO);
    }
    
    /**
     * 分页查询课程列表
     */
    @GetMapping
    @Operation(summary = "分页查询课程", description = "根据条件分页查询课程列表")
    public Result<IPage<CourseVO>> getCoursePage(@Parameter(description = "查询条件") CourseQueryRequest query) {
        log.info("分页查询课程请求: keyword={}, categoryId={}", query.getKeyword(), query.getCategoryId());
        IPage<CourseVO> page = courseService.getCoursePage(query);
        return Result.success("查询成功", page);
    }
    
    /**
     * 获取教师课程列表
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "获取教师课程", description = "根据教师ID获取课程列表")
    public Result<List<CourseVO>> getCoursesByTeacher(@Parameter(description = "教师ID") @PathVariable Long teacherId,
                                                    @Parameter(description = "课程状态") @RequestParam(required = false) String status) {
        log.info("获取教师课程请求: teacherId={}, status={}", teacherId, status);
        List<CourseVO> courses = courseService.getCoursesByTeacher(teacherId, status);
        return Result.success("查询成功", courses);
    }
    
    /**
     * 获取分类课程列表
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "获取分类课程", description = "根据分类ID获取课程列表")
    public Result<List<CourseVO>> getCoursesByCategory(@Parameter(description = "分类ID") @PathVariable Long categoryId,
                                                     @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取分类课程请求: categoryId={}, limit={}", categoryId, limit);
        List<CourseVO> courses = courseService.getCoursesByCategory(categoryId, limit);
        return Result.success("查询成功", courses);
    }
    
    /**
     * 获取推荐课程列表
     */
    @GetMapping("/recommended")
    @Operation(summary = "获取推荐课程", description = "获取推荐课程列表")
    public Result<List<CourseVO>> getRecommendedCourses(@Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取推荐课程请求: limit={}", limit);
        List<CourseVO> courses = courseService.getRecommendedCourses(limit);
        return Result.success("查询成功", courses);
    }
    
    /**
     * 获取热门课程列表
     */
    @GetMapping("/popular")
    @Operation(summary = "获取热门课程", description = "获取热门课程列表")
    public Result<List<CourseVO>> getPopularCourses(@Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取热门课程请求: limit={}", limit);
        List<CourseVO> courses = courseService.getPopularCourses(limit);
        return Result.success("查询成功", courses);
    }
    
    /**
     * 获取最新课程列表
     */
    @GetMapping("/latest")
    @Operation(summary = "获取最新课程", description = "获取最新课程列表")
    public Result<List<CourseVO>> getLatestCourses(@Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取最新课程请求: limit={}", limit);
        List<CourseVO> courses = courseService.getLatestCourses(limit);
        return Result.success("查询成功", courses);
    }
    
    /**
     * 搜索课程
     */
    @GetMapping("/search")
    @Operation(summary = "搜索课程", description = "根据关键词搜索课程")
    public Result<IPage<CourseVO>> searchCourses(@Parameter(description = "搜索关键词") @RequestParam String keyword,
                                               @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                               @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size) {
        log.info("搜索课程请求: keyword={}, page={}, size={}", keyword, page, size);
        IPage<CourseVO> result = courseService.searchCourses(keyword, page, size);
        return Result.success("搜索成功", result);
    }
    
    /**
     * 发布课程
     */
    @PostMapping("/{courseId}/publish")
    @Operation(summary = "发布课程", description = "教师发布课程")
    public Result<Void> publishCourse(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                    @RequestHeader("X-User-Id") Long teacherId) {
        log.info("发布课程请求: courseId={}, teacherId={}", courseId, teacherId);
        courseService.publishCourse(courseId, teacherId);
        return Result.success("课程发布成功");
    }
    
    /**
     * 下线课程
     */
    @PostMapping("/{courseId}/offline")
    @Operation(summary = "下线课程", description = "教师下线课程")
    public Result<Void> offlineCourse(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                    @RequestHeader("X-User-Id") Long teacherId) {
        log.info("下线课程请求: courseId={}, teacherId={}", courseId, teacherId);
        courseService.offlineCourse(courseId, teacherId);
        return Result.success("课程下线成功");
    }
    
    /**
     * 审核课程
     */
    @PostMapping("/{courseId}/review")
    @Operation(summary = "审核课程", description = "管理员审核课程")
    public Result<Void> reviewCourse(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                   @Parameter(description = "审核状态") @RequestParam String status,
                                   @Parameter(description = "审核备注") @RequestParam(required = false) String remark) {
        log.info("审核课程请求: courseId={}, status={}", courseId, status);
        courseService.reviewCourse(courseId, status, remark);
        return Result.success("课程审核成功");
    }
    
    /**
     * 增加学习人数
     */
    @PostMapping("/{courseId}/increment-student")
    @Operation(summary = "增加学习人数", description = "增加课程学习人数")
    public Result<Void> incrementStudentCount(@Parameter(description = "课程ID") @PathVariable Long courseId) {
        log.info("增加学习人数请求: courseId={}", courseId);
        courseService.incrementStudentCount(courseId);
        return Result.success("操作成功");
    }
    
    /**
     * 增加收藏数量
     */
    @PostMapping("/{courseId}/favorite")
    @Operation(summary = "收藏课程", description = "增加课程收藏数量")
    public Result<Void> favoriteCourse(@Parameter(description = "课程ID") @PathVariable Long courseId) {
        log.info("收藏课程请求: courseId={}", courseId);
        courseService.incrementFavoriteCount(courseId);
        return Result.success("收藏成功");
    }
    
    /**
     * 取消收藏
     */
    @DeleteMapping("/{courseId}/favorite")
    @Operation(summary = "取消收藏", description = "减少课程收藏数量")
    public Result<Void> unfavoriteCourse(@Parameter(description = "课程ID") @PathVariable Long courseId) {
        log.info("取消收藏请求: courseId={}", courseId);
        courseService.decrementFavoriteCount(courseId);
        return Result.success("取消收藏成功");
    }
    
    /**
     * 统计课程数量
     */
    @GetMapping("/count")
    @Operation(summary = "统计课程数量", description = "根据状态统计课程数量")
    public Result<Long> countCourses(@Parameter(description = "课程状态") @RequestParam(required = false) String status) {
        log.info("统计课程数量请求: status={}", status);
        Long count = courseService.countCoursesByStatus(status);
        return Result.success("统计成功", count);
    }
    
    /**
     * 统计教师课程数量
     */
    @GetMapping("/teacher/{teacherId}/count")
    @Operation(summary = "统计教师课程数量", description = "统计指定教师的课程数量")
    public Result<Long> countCoursesByTeacher(@Parameter(description = "教师ID") @PathVariable Long teacherId) {
        log.info("统计教师课程数量请求: teacherId={}", teacherId);
        Long count = courseService.countCoursesByTeacher(teacherId);
        return Result.success("统计成功", count);
    }
}
