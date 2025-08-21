package com.smartlearning.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.exam.entity.ExamRecord;
import com.smartlearning.exam.vo.ExamRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考试记录Mapper接口
 */
@Mapper
public interface ExamRecordMapper extends BaseMapper<ExamRecord> {
    
    /**
     * 分页查询考试记录
     */
    IPage<ExamRecordVO> selectRecordPage(Page<ExamRecordVO> page,
                                       @Param("paperId") Long paperId,
                                       @Param("userId") Long userId,
                                       @Param("status") String status);
    
    /**
     * 根据ID查询考试记录详情
     */
    ExamRecordVO selectRecordDetail(@Param("recordId") Long recordId);
    
    /**
     * 根据用户和试卷查询考试记录
     */
    List<ExamRecord> selectByUserAndPaper(@Param("userId") Long userId, @Param("paperId") Long paperId);
    
    /**
     * 查询用户最近考试记录
     */
    List<ExamRecordVO> selectRecentRecords(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 统计考试记录
     */
    Long countRecords(@Param("paperId") Long paperId, 
                     @Param("userId") Long userId,
                     @Param("status") String status);
    
    /**
     * 计算平均分
     */
    Double calculateAverageScore(@Param("paperId") Long paperId);
    
    /**
     * 计算通过率
     */
    Double calculatePassRate(@Param("paperId") Long paperId, @Param("passScore") Integer passScore);
    
    /**
     * 更新排名
     */
    int updateRanking(@Param("paperId") Long paperId);
}
