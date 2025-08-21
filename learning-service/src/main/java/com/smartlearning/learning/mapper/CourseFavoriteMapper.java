package com.smartlearning.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.learning.entity.CourseFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程收藏Mapper接口
 */
@Mapper
public interface CourseFavoriteMapper extends BaseMapper<CourseFavorite> {
    
    /**
     * 分页查询用户收藏
     */
    IPage<CourseFavorite> selectUserFavoritesPage(Page<CourseFavorite> page, @Param("userId") Long userId);
    
    /**
     * 根据分组查询收藏
     */
    List<CourseFavorite> selectByCategory(@Param("userId") Long userId, @Param("category") String category);
    
    /**
     * 检查是否已收藏
     */
    CourseFavorite selectByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    /**
     * 统计用户收藏数量
     */
    Integer countUserFavorites(@Param("userId") Long userId);
    
    /**
     * 查询收藏分组
     */
    List<String> selectCategories(@Param("userId") Long userId);
}
