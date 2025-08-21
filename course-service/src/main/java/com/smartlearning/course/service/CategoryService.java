package com.smartlearning.course.service;

import com.smartlearning.course.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {
    
    /**
     * 获取分类树
     */
    List<Category> getCategoryTree();
    
    /**
     * 根据父ID获取子分类
     */
    List<Category> getChildrenCategories(Long parentId);
    
    /**
     * 根据ID查询分类
     */
    Category findById(Long categoryId);
    
    /**
     * 创建分类
     */
    Category createCategory(Category category);
    
    /**
     * 更新分类
     */
    Category updateCategory(Long categoryId, Category category);
    
    /**
     * 删除分类
     */
    boolean deleteCategory(Long categoryId);
    
    /**
     * 更新分类课程数量
     */
    boolean updateCourseCount(Long categoryId, Integer courseCount);
}
