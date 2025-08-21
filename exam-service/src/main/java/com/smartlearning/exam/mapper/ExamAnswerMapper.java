package com.smartlearning.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.exam.entity.ExamAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 答题记录Mapper接口
 */
@Mapper
public interface ExamAnswerMapper extends BaseMapper<ExamAnswer> {
    
    /**
     * 根据考试记录ID查询答题记录
     */
    List<ExamAnswer> selectByRecordId(@Param("recordId") Long recordId);
    
    /**
     * 批量插入答题记录
     */
    int batchInsert(@Param("examAnswers") List<ExamAnswer> examAnswers);
    
    /**
     * 批量更新答题记录
     */
    int batchUpdate(@Param("examAnswers") List<ExamAnswer> examAnswers);
    
    /**
     * 统计正确题数
     */
    Integer countCorrectAnswers(@Param("recordId") Long recordId);
    
    /**
     * 统计错误题数
     */
    Integer countWrongAnswers(@Param("recordId") Long recordId);
    
    /**
     * 计算总得分
     */
    Integer sumScore(@Param("recordId") Long recordId);
    
    /**
     * 根据题目统计正确率
     */
    Double calculateQuestionAccuracy(@Param("questionId") Long questionId);
}
