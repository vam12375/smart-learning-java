package com.smartlearning.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.course.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类Mapper接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    
    /**
     * 查询所有分类树
     */
    List<Category> selectCategoryTree();
    
    /**
     * 根据父ID查询子分类
     */
    List<Category> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 更新分类课程数量
     */
    int updateCourseCount(@Param("categoryId") Long categoryId, @Param("courseCount") Integer courseCount);
    
    /**
     * 增加分类课程数量
     */
    int incrementCourseCount(@Param("categoryId") Long categoryId);
    
    /**
     * 减少分类课程数量
     */
    int decrementCourseCount(@Param("categoryId") Long categoryId);
}
