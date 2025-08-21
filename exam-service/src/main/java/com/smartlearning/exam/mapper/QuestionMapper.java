package com.smartlearning.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.exam.entity.Question;
import com.smartlearning.exam.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目Mapper接口
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
    
    /**
     * 分页查询题目
     */
    IPage<QuestionVO> selectQuestionPage(Page<QuestionVO> page, 
                                       @Param("courseId") Long courseId,
                                       @Param("type") String type,
                                       @Param("difficulty") String difficulty,
                                       @Param("keyword") String keyword);
    
    /**
     * 根据课程ID查询题目
     */
    List<QuestionVO> selectByCourse(@Param("courseId") Long courseId);
    
    /**
     * 根据章节ID查询题目
     */
    List<QuestionVO> selectByChapter(@Param("chapterId") Long chapterId);
    
    /**
     * 随机查询题目
     */
    List<QuestionVO> selectRandomQuestions(@Param("courseId") Long courseId,
                                         @Param("type") String type,
                                         @Param("difficulty") String difficulty,
                                         @Param("limit") Integer limit);
    
    /**
     * 更新题目使用次数
     */
    int incrementUseCount(@Param("questionId") Long questionId);
    
    /**
     * 更新题目正确率
     */
    int updateAccuracy(@Param("questionId") Long questionId, @Param("accuracy") Double accuracy);
    
    /**
     * 统计题目数量
     */
    Long countQuestions(@Param("courseId") Long courseId, 
                       @Param("type") String type,
                       @Param("difficulty") String difficulty);
}
