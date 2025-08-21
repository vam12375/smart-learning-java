package com.smartlearning.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartlearning.course.dto.CourseCreateRequest;
import com.smartlearning.course.dto.CourseQueryRequest;
import com.smartlearning.course.entity.Course;
import com.smartlearning.course.vo.CourseVO;

import java.util.List;

/**
 * 课程服务接口
 */
public interface CourseService {
    
    /**
     * 创建课程
     */
    Course createCourse(CourseCreateRequest request, Long teacherId);
    
    /**
     * 更新课程
     */
    Course updateCourse(Long courseId, CourseCreateRequest request, Long teacherId);
    
    /**
     * 删除课程
     */
    boolean deleteCourse(Long courseId, Long teacherId);
    
    /**
     * 根据ID查询课程
     */
    Course findById(Long courseId);
    
    /**
     * 查询课程详情
     */
    CourseVO getCourseDetail(Long courseId);
    
    /**
     * 分页查询课程列表
     */
    IPage<CourseVO> getCoursePage(CourseQueryRequest query);
    
    /**
     * 根据教师ID查询课程列表
     */
    List<CourseVO> getCoursesByTeacher(Long teacherId, String status);
    
    /**
     * 根据分类ID查询课程列表
     */
    List<CourseVO> getCoursesByCategory(Long categoryId, Integer limit);
    
    /**
     * 查询推荐课程列表
     */
    List<CourseVO> getRecommendedCourses(Integer limit);
    
    /**
     * 查询热门课程列表
     */
    List<CourseVO> getPopularCourses(Integer limit);
    
    /**
     * 查询最新课程列表
     */
    List<CourseVO> getLatestCourses(Integer limit);
    
    /**
     * 搜索课程
     */
    IPage<CourseVO> searchCourses(String keyword, Integer page, Integer size);
    
    /**
     * 发布课程
     */
    boolean publishCourse(Long courseId, Long teacherId);
    
    /**
     * 下线课程
     */
    boolean offlineCourse(Long courseId, Long teacherId);
    
    /**
     * 审核课程
     */
    boolean reviewCourse(Long courseId, String status, String remark);
    
    /**
     * 增加学习人数
     */
    boolean incrementStudentCount(Long courseId);
    
    /**
     * 增加收藏数量
     */
    boolean incrementFavoriteCount(Long courseId);
    
    /**
     * 减少收藏数量
     */
    boolean decrementFavoriteCount(Long courseId);
    
    /**
     * 更新课程评分
     */
    boolean updateCourseRating(Long courseId, java.math.BigDecimal rating, Integer reviewCount);
    
    /**
     * 检查课程权限
     */
    boolean checkCoursePermission(Long courseId, Long teacherId);
    
    /**
     * 统计课程数量
     */
    Long countCoursesByStatus(String status);
    
    /**
     * 统计教师课程数量
     */
    Long countCoursesByTeacher(Long teacherId);
}
