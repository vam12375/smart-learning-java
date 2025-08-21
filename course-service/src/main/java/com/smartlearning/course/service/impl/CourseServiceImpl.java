package com.smartlearning.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.common.constant.CommonConstants;
import com.smartlearning.common.exception.BusinessException;
import com.smartlearning.common.result.ResultCode;
import com.smartlearning.course.dto.CourseCreateRequest;
import com.smartlearning.course.dto.CourseQueryRequest;
import com.smartlearning.course.entity.Course;
import com.smartlearning.course.mapper.CategoryMapper;
import com.smartlearning.course.mapper.CourseMapper;
import com.smartlearning.course.service.CourseService;
import com.smartlearning.course.vo.CourseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 课程服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final CategoryMapper categoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Course createCourse(CourseCreateRequest request, Long teacherId) {
        log.info("创建课程请求: title={}, teacherId={}", request.getTitle(), teacherId);

        // 验证分类是否存在
        if (categoryMapper.selectById(request.getCategoryId()) == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "课程分类不存在");
        }

        // 创建课程对象
        Course course = new Course();
        BeanUtils.copyProperties(request, course);
        course.setTeacherId(teacherId);
        course.setStatus(CommonConstants.CourseStatus.DRAFT);
        course.setStudentCount(0);
        course.setFavoriteCount(0);
        course.setRating(BigDecimal.ZERO);
        course.setReviewCount(0);
        course.setDuration(0);
        course.setLessonCount(0);
        course.setCreateTime(LocalDateTime.now());
        course.setUpdateTime(LocalDateTime.now());

        // 处理标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            course.setTags(String.join(",", request.getTags()));
        }

        // 保存课程
        int result = courseMapper.insert(course);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "课程创建失败");
        }

        // 增加分类课程数量
        categoryMapper.incrementCourseCount(request.getCategoryId());

        log.info("课程创建成功: courseId={}, title={}", course.getId(), course.getTitle());
        return course;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Course updateCourse(Long courseId, CourseCreateRequest request, Long teacherId) {
        log.info("更新课程请求: courseId={}, teacherId={}", courseId, teacherId);

        // 检查课程是否存在和权限
        Course existingCourse = findById(courseId);
        if (existingCourse == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "课程不存在");
        }

        if (!checkCoursePermission(courseId, teacherId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此课程");
        }

        // 验证分类是否存在
        if (categoryMapper.selectById(request.getCategoryId()) == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "课程分类不存在");
        }

        // 更新课程信息
        Course course = new Course();
        BeanUtils.copyProperties(request, course);
        course.setId(courseId);
        course.setUpdateTime(LocalDateTime.now());

        // 处理标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            course.setTags(String.join(",", request.getTags()));
        }

        // 更新课程
        int result = courseMapper.updateById(course);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "课程更新失败");
        }

        // 如果分类发生变化，更新分类课程数量
        if (!existingCourse.getCategoryId().equals(request.getCategoryId())) {
            categoryMapper.decrementCourseCount(existingCourse.getCategoryId());
            categoryMapper.incrementCourseCount(request.getCategoryId());
        }

        // 清除缓存
        clearCourseCache(courseId);

        log.info("课程更新成功: courseId={}", courseId);
        return findById(courseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCourse(Long courseId, Long teacherId) {
        log.info("删除课程请求: courseId={}, teacherId={}", courseId, teacherId);

        // 检查课程是否存在和权限
        Course course = findById(courseId);
        if (course == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "课程不存在");
        }

        if (!checkCoursePermission(courseId, teacherId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此课程");
        }

        // 检查课程状态，已发布的课程不能删除
        if (CommonConstants.CourseStatus.PUBLISHED.equals(course.getStatus())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "已发布的课程不能删除");
        }

        // 软删除课程
        int result = courseMapper.deleteById(courseId);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "课程删除失败");
        }

        // 减少分类课程数量
        categoryMapper.decrementCourseCount(course.getCategoryId());

        // 清除缓存
        clearCourseCache(courseId);

        log.info("课程删除成功: courseId={}", courseId);
        return true;
    }

    @Override
    public Course findById(Long courseId) {
        if (courseId == null) {
            return null;
        }

        // 先从缓存查询
        String cacheKey = CommonConstants.CachePrefix.COURSE_INFO + courseId;
        Course cachedCourse = (Course) redisTemplate.opsForValue().get(cacheKey);
        if (cachedCourse != null) {
            return cachedCourse;
        }

        // 从数据库查询
        Course course = courseMapper.selectById(courseId);

        // 缓存查询结果
        if (course != null) {
            redisTemplate.opsForValue().set(cacheKey, course,
                    CommonConstants.CacheExpire.COURSE_INFO, TimeUnit.SECONDS);
        }

        return course;
    }

    @Override
    public CourseVO getCourseDetail(Long courseId) {
        if (courseId == null) {
            return null;
        }

        // 先从缓存查询
        String cacheKey = "course:detail:" + courseId;
        CourseVO cachedCourse = (CourseVO) redisTemplate.opsForValue().get(cacheKey);
        if (cachedCourse != null) {
            return cachedCourse;
        }

        // 从数据库查询
        CourseVO courseVO = courseMapper.selectCourseDetail(courseId);

        // 缓存查询结果
        if (courseVO != null) {
            redisTemplate.opsForValue().set(cacheKey, courseVO,
                    CommonConstants.CacheExpire.COURSE_INFO, TimeUnit.SECONDS);
        }

        return courseVO;
    }

    @Override
    public IPage<CourseVO> getCoursePage(CourseQueryRequest query) {
        Page<CourseVO> page = new Page<>(query.getPage(), query.getSize());
        return courseMapper.selectCoursePage(page, query);
    }

    @Override
    public List<CourseVO> getCoursesByTeacher(Long teacherId, String status) {
        return courseMapper.selectCoursesByTeacher(teacherId, status);
    }

    @Override
    public List<CourseVO> getCoursesByCategory(Long categoryId, Integer limit) {
        return courseMapper.selectCoursesByCategory(categoryId, limit);
    }

    @Override
    public List<CourseVO> getRecommendedCourses(Integer limit) {
        // 先从缓存查询
        String cacheKey = "course:recommended:" + limit;
        @SuppressWarnings("unchecked")
        List<CourseVO> cachedCourses = (List<CourseVO>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedCourses != null) {
            return cachedCourses;
        }

        // 从数据库查询
        List<CourseVO> courses = courseMapper.selectRecommendedCourses(limit);

        // 缓存查询结果
        if (courses != null && !courses.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, courses,
                    CommonConstants.CacheExpire.HOT_DATA, TimeUnit.SECONDS);
        }

        return courses;
    }

    @Override
    public List<CourseVO> getPopularCourses(Integer limit) {
        // 先从缓存查询
        String cacheKey = "course:popular:" + limit;
        @SuppressWarnings("unchecked")
        List<CourseVO> cachedCourses = (List<CourseVO>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedCourses != null) {
            return cachedCourses;
        }

        // 从数据库查询
        List<CourseVO> courses = courseMapper.selectPopularCourses(limit);

        // 缓存查询结果
        if (courses != null && !courses.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, courses,
                    CommonConstants.CacheExpire.HOT_DATA, TimeUnit.SECONDS);
        }

        return courses;
    }

    @Override
    public List<CourseVO> getLatestCourses(Integer limit) {
        return courseMapper.selectLatestCourses(limit);
    }

    @Override
    public IPage<CourseVO> searchCourses(String keyword, Integer page, Integer size) {
        Page<CourseVO> pageParam = new Page<>(page, size);
        return courseMapper.searchCourses(pageParam, keyword);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishCourse(Long courseId, Long teacherId) {
        log.info("发布课程请求: courseId={}, teacherId={}", courseId, teacherId);

        // 检查课程权限
        if (!checkCoursePermission(courseId, teacherId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此课程");
        }

        // 更新课程状态
        int result = courseMapper.updateCourseStatus(courseId, CommonConstants.CourseStatus.REVIEWING);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "课程发布失败");
        }

        // 清除缓存
        clearCourseCache(courseId);

        log.info("课程发布成功: courseId={}", courseId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean offlineCourse(Long courseId, Long teacherId) {
        log.info("下线课程请求: courseId={}, teacherId={}", courseId, teacherId);

        // 检查课程权限
        if (!checkCoursePermission(courseId, teacherId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此课程");
        }

        // 更新课程状态
        int result = courseMapper.updateCourseStatus(courseId, CommonConstants.CourseStatus.OFFLINE);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "课程下线失败");
        }

        // 清除缓存
        clearCourseCache(courseId);

        log.info("课程下线成功: courseId={}", courseId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewCourse(Long courseId, String status, String remark) {
        log.info("审核课程请求: courseId={}, status={}", courseId, status);

        // 验证状态
        if (!CommonConstants.CourseStatus.PUBLISHED.equals(status) &&
                !CommonConstants.CourseStatus.DRAFT.equals(status)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "审核状态不正确");
        }

        // 更新课程状态
        Course course = new Course();
        course.setId(courseId);
        course.setStatus(status);
        course.setUpdateTime(LocalDateTime.now());

        if (CommonConstants.CourseStatus.PUBLISHED.equals(status)) {
            course.setPublishTime(LocalDateTime.now());
        }

        int result = courseMapper.updateById(course);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "课程审核失败");
        }

        // 清除缓存
        clearCourseCache(courseId);

        log.info("课程审核成功: courseId={}, status={}", courseId, status);
        return true;
    }

    @Override
    public boolean incrementStudentCount(Long courseId) {
        try {
            int result = courseMapper.incrementStudentCount(courseId);
            if (result > 0) {
                clearCourseCache(courseId);
                return true;
            }
        } catch (Exception e) {
            log.error("增加学习人数失败: courseId={}", courseId, e);
        }
        return false;
    }

    @Override
    public boolean incrementFavoriteCount(Long courseId) {
        try {
            int result = courseMapper.incrementFavoriteCount(courseId);
            if (result > 0) {
                clearCourseCache(courseId);
                return true;
            }
        } catch (Exception e) {
            log.error("增加收藏数量失败: courseId={}", courseId, e);
        }
        return false;
    }

    @Override
    public boolean decrementFavoriteCount(Long courseId) {
        try {
            int result = courseMapper.decrementFavoriteCount(courseId);
            if (result > 0) {
                clearCourseCache(courseId);
                return true;
            }
        } catch (Exception e) {
            log.error("减少收藏数量失败: courseId={}", courseId, e);
        }
        return false;
    }

    @Override
    public boolean updateCourseRating(Long courseId, BigDecimal rating, Integer reviewCount) {
        try {
            int result = courseMapper.updateCourseStats(courseId, null, null, reviewCount, rating);
            if (result > 0) {
                clearCourseCache(courseId);
                return true;
            }
        } catch (Exception e) {
            log.error("更新课程评分失败: courseId={}", courseId, e);
        }
        return false;
    }

    @Override
    public boolean checkCoursePermission(Long courseId, Long teacherId) {
        if (courseId == null || teacherId == null) {
            return false;
        }

        Course course = findById(courseId);
        return course != null && course.getTeacherId().equals(teacherId);
    }

    @Override
    public Long countCoursesByStatus(String status) {
        return courseMapper.countCoursesByStatus(status);
    }

    @Override
    public Long countCoursesByTeacher(Long teacherId) {
        return courseMapper.countCoursesByTeacher(teacherId);
    }

    /**
     * 清除课程缓存
     */
    private void clearCourseCache(Long courseId) {
        try {
            String courseInfoKey = CommonConstants.CachePrefix.COURSE_INFO + courseId;
            String courseDetailKey = "course:detail:" + courseId;

            redisTemplate.delete(courseInfoKey);
            redisTemplate.delete(courseDetailKey);

            // 清除相关列表缓存
            redisTemplate.delete("course:recommended:*");
            redisTemplate.delete("course:popular:*");
        } catch (Exception e) {
            log.warn("清除课程缓存失败: courseId={}, error={}", courseId, e.getMessage());
        }
    }
}
