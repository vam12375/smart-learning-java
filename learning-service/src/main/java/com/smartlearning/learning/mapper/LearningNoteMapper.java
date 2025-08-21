package com.smartlearning.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.learning.entity.LearningNote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习笔记Mapper接口
 */
@Mapper
public interface LearningNoteMapper extends BaseMapper<LearningNote> {
    
    /**
     * 分页查询用户笔记
     */
    IPage<LearningNote> selectUserNotesPage(Page<LearningNote> page, @Param("userId") Long userId);
    
    /**
     * 根据课程ID查询用户笔记
     */
    List<LearningNote> selectByCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);
    
    /**
     * 根据课时ID查询用户笔记
     */
    List<LearningNote> selectByLesson(@Param("userId") Long userId, @Param("lessonId") Long lessonId);
    
    /**
     * 查询公开笔记
     */
    IPage<LearningNote> selectPublicNotesPage(Page<LearningNote> page, @Param("courseId") Long courseId);
    
    /**
     * 增加笔记点赞数
     */
    int incrementLikeCount(@Param("noteId") Long noteId);
    
    /**
     * 减少笔记点赞数
     */
    int decrementLikeCount(@Param("noteId") Long noteId);
    
    /**
     * 统计用户笔记数量
     */
    Integer countUserNotes(@Param("userId") Long userId);
    
    /**
     * 搜索笔记
     */
    IPage<LearningNote> searchNotes(Page<LearningNote> page, 
                                   @Param("userId") Long userId,
                                   @Param("keyword") String keyword);
}
