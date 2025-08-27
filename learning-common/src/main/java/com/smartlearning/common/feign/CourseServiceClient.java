package com.smartlearning.common.feign;

import com.smartlearning.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 课程服务Feign客户端
 */
@FeignClient(name = "course-service", path = "/api/course")
public interface CourseServiceClient {
    
    /**
     * 根据ID获取课程信息
     */
    @GetMapping("/{courseId}")
    Result<Map<String, Object>> getCourseById(@PathVariable("courseId") Long courseId);
    
    /**
     * 批量获取课程信息
     */
    @GetMapping("/batch")
    Result<List<Map<String, Object>>> getCoursesByIds(@RequestParam("courseIds") List<Long> courseIds);
    
    /**
     * 获取热门课程
     */
    @GetMapping("/popular")
    Result<List<Map<String, Object>>> getPopularCourses(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                                       @RequestParam(value = "count", defaultValue = "10") Integer count);
    
    /**
     * 根据分类获取课程
     */
    @GetMapping("/category/{categoryId}")
    Result<List<Map<String, Object>>> getCoursesByCategory(@PathVariable("categoryId") Long categoryId,
                                                          @RequestParam(value = "count", defaultValue = "20") Integer count);
    
    /**
     * 搜索课程
     */
    @GetMapping("/search")
    Result<List<Map<String, Object>>> searchCourses(@RequestParam("keyword") String keyword,
                                                   @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                   @RequestParam(value = "difficulty", required = false) String difficulty,
                                                   @RequestParam(value = "count", defaultValue = "20") Integer count);
}
