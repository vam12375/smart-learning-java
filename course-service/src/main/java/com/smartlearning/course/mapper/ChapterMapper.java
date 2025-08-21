package com.smartlearning.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.course.entity.Chapter;
import com.smartlearning.course.vo.ChapterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 章节Mapper接口
 */
@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {
    
    /**
     * 根据课程ID查询章节列表
     */
    List<ChapterVO> selectChaptersByCourseId(@Param("courseId") Long courseId);
    
    /**
     * 更新章节课时数量
     */
    int updateLessonCount(@Param("chapterId") Long chapterId, @Param("lessonCount") Integer lessonCount);
    
    /**
     * 更新章节时长
     */
    int updateDuration(@Param("chapterId") Long chapterId, @Param("duration") Integer duration);
}
