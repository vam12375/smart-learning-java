package com.smartlearning.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.exam.entity.ExamPaper;
import com.smartlearning.exam.vo.ExamPaperVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷Mapper接口
 */
@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {
    
    /**
     * 分页查询试卷
     */
    IPage<ExamPaperVO> selectPaperPage(Page<ExamPaperVO> page,
                                     @Param("courseId") Long courseId,
                                     @Param("type") String type,
                                     @Param("status") String status,
                                     @Param("keyword") String keyword);
    
    /**
     * 根据ID查询试卷详情
     */
    ExamPaperVO selectPaperDetail(@Param("paperId") Long paperId);
    
    /**
     * 根据课程ID查询试卷
     */
    List<ExamPaperVO> selectByCourse(@Param("courseId") Long courseId, @Param("status") String status);
    
    /**
     * 根据创建者查询试卷
     */
    List<ExamPaperVO> selectByCreator(@Param("creatorId") Long creatorId, @Param("status") String status);
    
    /**
     * 更新试卷统计信息
     */
    int updatePaperStats(@Param("paperId") Long paperId,
                        @Param("participantCount") Integer participantCount,
                        @Param("averageScore") Double averageScore,
                        @Param("passRate") Double passRate);
    
    /**
     * 增加参考人数
     */
    int incrementParticipantCount(@Param("paperId") Long paperId);
    
    /**
     * 统计试卷数量
     */
    Long countPapers(@Param("courseId") Long courseId, 
                    @Param("type") String type,
                    @Param("status") String status);
}
