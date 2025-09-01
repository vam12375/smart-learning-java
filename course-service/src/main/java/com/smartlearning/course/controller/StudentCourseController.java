package com.smartlearning.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartlearning.common.result.Result;
import com.smartlearning.course.entity.Category;
import com.smartlearning.course.entity.Course;
import com.smartlearning.course.service.CategoryService;
import com.smartlearning.course.service.CourseService;
import com.smartlearning.course.vo.CourseVO;
import com.smartlearning.course.dto.CourseQueryRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生端课程控制器
 * 提供学生端课程相关接口，匹配前端API调用
 */
@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "学生端课程管理", description = "学生端课程查询、分类等相关接口")
public class StudentCourseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    /**
     * 获取课程分类
     */
    @GetMapping("/categories")
    @Operation(summary = "获取课程分类", description = "获取所有课程分类列表")
    public Result<List<Category>> getCategories() {
        log.info("获取课程分类请求");
        List<Category> categories = categoryService.getCategoryTree();
        return Result.success("查询成功", categories);
    }

    /**
     * 获取课程列表
     */
    @GetMapping
    @Operation(summary = "获取课程列表", description = "分页查询课程列表")
    public Result<IPage<CourseVO>> getCourses(@Parameter(description = "查询条件") CourseQueryRequest query) {
        log.info("获取课程列表请求: keyword={}, categoryId={}", query.getKeyword(), query.getCategoryId());
        IPage<CourseVO> page = courseService.getCoursePage(query);
        return Result.success("查询成功", page);
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
     * 获取分类下的课程
     */
    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "获取分类课程", description = "根据分类ID获取课程列表")
    public Result<List<CourseVO>> getCoursesByCategory(@Parameter(description = "分类ID") @PathVariable Long categoryId,
            @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取分类课程请求: categoryId={}, limit={}", categoryId, limit);
        List<CourseVO> courses = courseService.getCoursesByCategory(categoryId, limit);
        return Result.success("查询成功", courses);
    }

    /**
     * 获取推荐课程
     */
    @GetMapping("/recommended")
    @Operation(summary = "获取推荐课程", description = "获取推荐课程列表")
    public Result<List<CourseVO>> getRecommendedCourses(
            @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取推荐课程请求: limit={}", limit);
        // 这里可以实现推荐算法，暂时返回最新的课程
        List<CourseVO> courses = courseService.getLatestCourses(limit);
        return Result.success("查询成功", courses);
    }

    /**
     * 获取热门课程
     */
    @GetMapping("/popular")
    @Operation(summary = "获取热门课程", description = "获取热门课程列表")
    public Result<List<CourseVO>> getPopularCourses(
            @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取热门课程请求: limit={}", limit);
        List<CourseVO> courses = courseService.getPopularCourses(limit);
        return Result.success("查询成功", courses);
    }

    /**
     * 获取最新课程
     */
    @GetMapping("/latest")
    @Operation(summary = "获取最新课程", description = "获取最新课程列表")
    public Result<List<CourseVO>> getLatestCourses(
            @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取最新课程请求: limit={}", limit);
        List<CourseVO> courses = courseService.getLatestCourses(limit);
        return Result.success("查询成功", courses);
    }

    /**
     * 获取免费课程
     */
    @GetMapping("/free")
    @Operation(summary = "获取免费课程", description = "获取免费课程列表")
    public Result<IPage<CourseVO>> getFreeCourses(@Parameter(description = "查询条件") CourseQueryRequest query) {
        log.info("获取免费课程请求");
        // 设置价格为0来查询免费课程
        query.setMinPrice(java.math.BigDecimal.ZERO);
        query.setMaxPrice(java.math.BigDecimal.ZERO);
        IPage<CourseVO> page = courseService.getCoursePage(query);
        return Result.success("查询成功", page);
    }

    /**
     * 搜索课程
     */
    @GetMapping("/search")
    @Operation(summary = "搜索课程", description = "根据关键词搜索课程")
    public Result<IPage<CourseVO>> searchCourses(@Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Parameter(description = "查询条件") CourseQueryRequest query) {
        log.info("搜索课程请求: keyword={}", keyword);
        query.setKeyword(keyword);
        IPage<CourseVO> page = courseService.getCoursePage(query);
        return Result.success("查询成功", page);
    }
}
