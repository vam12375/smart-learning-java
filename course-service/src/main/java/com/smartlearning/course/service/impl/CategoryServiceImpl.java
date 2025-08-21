package com.smartlearning.course.service.impl;

import com.smartlearning.common.constant.CommonConstants;
import com.smartlearning.common.exception.BusinessException;
import com.smartlearning.common.result.ResultCode;
import com.smartlearning.course.entity.Category;
import com.smartlearning.course.mapper.CategoryMapper;
import com.smartlearning.course.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分类服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryMapper categoryMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public List<Category> getCategoryTree() {
        // 先从缓存查询
        String cacheKey = "category:tree";
        @SuppressWarnings("unchecked")
        List<Category> cachedCategories = (List<Category>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedCategories != null) {
            return cachedCategories;
        }
        
        // 从数据库查询
        List<Category> categories = categoryMapper.selectCategoryTree();
        
        // 缓存查询结果
        if (categories != null && !categories.isEmpty()) {
            redisTemplate.opsForValue().set(cacheKey, categories, 
                    CommonConstants.CacheExpire.HOT_DATA, TimeUnit.SECONDS);
        }
        
        return categories;
    }
    
    @Override
    public List<Category> getChildrenCategories(Long parentId) {
        return categoryMapper.selectByParentId(parentId);
    }
    
    @Override
    public Category findById(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        
        // 先从缓存查询
        String cacheKey = "category:info:" + categoryId;
        Category cachedCategory = (Category) redisTemplate.opsForValue().get(cacheKey);
        if (cachedCategory != null) {
            return cachedCategory;
        }
        
        // 从数据库查询
        Category category = categoryMapper.selectById(categoryId);
        
        // 缓存查询结果
        if (category != null) {
            redisTemplate.opsForValue().set(cacheKey, category, 
                    CommonConstants.CacheExpire.HOT_DATA, TimeUnit.SECONDS);
        }
        
        return category;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category createCategory(Category category) {
        log.info("创建分类请求: name={}", category.getName());
        
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCourseCount(0);
        
        int result = categoryMapper.insert(category);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "分类创建失败");
        }
        
        // 清除缓存
        clearCategoryCache();
        
        log.info("分类创建成功: categoryId={}, name={}", category.getId(), category.getName());
        return category;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category updateCategory(Long categoryId, Category category) {
        log.info("更新分类请求: categoryId={}", categoryId);
        
        Category existingCategory = findById(categoryId);
        if (existingCategory == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "分类不存在");
        }
        
        category.setId(categoryId);
        category.setUpdateTime(LocalDateTime.now());
        
        int result = categoryMapper.updateById(category);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "分类更新失败");
        }
        
        // 清除缓存
        clearCategoryCache();
        
        log.info("分类更新成功: categoryId={}", categoryId);
        return findById(categoryId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCategory(Long categoryId) {
        log.info("删除分类请求: categoryId={}", categoryId);
        
        Category category = findById(categoryId);
        if (category == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "分类不存在");
        }
        
        // 检查是否有课程
        if (category.getCourseCount() != null && category.getCourseCount() > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "该分类下还有课程，不能删除");
        }
        
        int result = categoryMapper.deleteById(categoryId);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "分类删除失败");
        }
        
        // 清除缓存
        clearCategoryCache();
        
        log.info("分类删除成功: categoryId={}", categoryId);
        return true;
    }
    
    @Override
    public boolean updateCourseCount(Long categoryId, Integer courseCount) {
        try {
            int result = categoryMapper.updateCourseCount(categoryId, courseCount);
            if (result > 0) {
                clearCategoryCache();
                return true;
            }
        } catch (Exception e) {
            log.error("更新分类课程数量失败: categoryId={}", categoryId, e);
        }
        return false;
    }
    
    /**
     * 清除分类缓存
     */
    private void clearCategoryCache() {
        try {
            redisTemplate.delete("category:tree");
            // 清除所有分类信息缓存
            redisTemplate.delete("category:info:*");
        } catch (Exception e) {
            log.warn("清除分类缓存失败: error={}", e.getMessage());
        }
    }
}
