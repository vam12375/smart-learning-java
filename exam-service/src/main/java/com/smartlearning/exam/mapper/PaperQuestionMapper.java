package com.smartlearning.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.exam.entity.PaperQuestion;
import com.smartlearning.exam.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷题目关联Mapper接口
 */
@Mapper
public interface PaperQuestionMapper extends BaseMapper<PaperQuestion> {
    
    /**
     * 根据试卷ID查询题目列表
     */
    List<QuestionVO> selectQuestionsByPaper(@Param("paperId") Long paperId);
    
    /**
     * 批量插入试卷题目
     */
    int batchInsert(@Param("paperQuestions") List<PaperQuestion> paperQuestions);
    
    /**
     * 根据试卷ID删除题目
     */
    int deleteByPaperId(@Param("paperId") Long paperId);
    
    /**
     * 统计试卷题目数量
     */
    Integer countByPaperId(@Param("paperId") Long paperId);
    
    /**
     * 计算试卷总分
     */
    Integer sumScoreByPaperId(@Param("paperId") Long paperId);
}
