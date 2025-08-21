package com.smartlearning.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.course.dto.CourseQueryRequest;
import com.smartlearning.course.entity.Course;
import com.smartlearning.course.vo.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    
    /**
     * 分页查询课程列表
     */
    IPage<CourseVO> selectCoursePage(Page<CourseVO> page, @Param("query") CourseQueryRequest query);
    
    /**
     * 根据ID查询课程详情
     */
    CourseVO selectCourseDetail(@Param("courseId") Long courseId);
    
    /**
     * 根据教师ID查询课程列表
     */
    List<CourseVO> selectCoursesByTeacher(@Param("teacherId") Long teacherId, @Param("status") String status);
    
    /**
     * 根据分类ID查询课程列表
     */
    List<CourseVO> selectCoursesByCategory(@Param("categoryId") Long categoryId, @Param("limit") Integer limit);
    
    /**
     * 查询推荐课程列表
     */
    List<CourseVO> selectRecommendedCourses(@Param("limit") Integer limit);
    
    /**
     * 查询热门课程列表
     */
    List<CourseVO> selectPopularCourses(@Param("limit") Integer limit);
    
    /**
     * 查询最新课程列表
     */
    List<CourseVO> selectLatestCourses(@Param("limit") Integer limit);
    
    /**
     * 更新课程统计信息
     */
    int updateCourseStats(@Param("courseId") Long courseId, 
                         @Param("studentCount") Integer studentCount,
                         @Param("favoriteCount") Integer favoriteCount,
                         @Param("reviewCount") Integer reviewCount,
                         @Param("rating") java.math.BigDecimal rating);
    
    /**
     * 更新课程状态
     */
    int updateCourseStatus(@Param("courseId") Long courseId, @Param("status") String status);
    
    /**
     * 增加学习人数
     */
    int incrementStudentCount(@Param("courseId") Long courseId);
    
    /**
     * 增加收藏数量
     */
    int incrementFavoriteCount(@Param("courseId") Long courseId);
    
    /**
     * 减少收藏数量
     */
    int decrementFavoriteCount(@Param("courseId") Long courseId);
    
    /**
     * 搜索课程
     */
    IPage<CourseVO> searchCourses(Page<CourseVO> page, @Param("keyword") String keyword);
    
    /**
     * 统计课程数量
     */
    Long countCoursesByStatus(@Param("status") String status);
    
    /**
     * 统计教师课程数量
     */
    Long countCoursesByTeacher(@Param("teacherId") Long teacherId);
}
