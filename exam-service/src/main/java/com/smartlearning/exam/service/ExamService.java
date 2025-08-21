package com.smartlearning.exam.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartlearning.exam.dto.ExamPaperCreateRequest;
import com.smartlearning.exam.dto.ExamSubmitRequest;
import com.smartlearning.exam.dto.QuestionCreateRequest;
import com.smartlearning.exam.entity.ExamPaper;
import com.smartlearning.exam.entity.ExamRecord;
import com.smartlearning.exam.entity.Question;
import com.smartlearning.exam.vo.ExamPaperVO;
import com.smartlearning.exam.vo.ExamRecordVO;
import com.smartlearning.exam.vo.QuestionVO;

import java.util.List;

/**
 * 考试服务接口
 */
public interface ExamService {
    
    // ==================== 题目管理 ====================
    
    /**
     * 创建题目
     */
    Question createQuestion(QuestionCreateRequest request, Long creatorId);
    
    /**
     * 更新题目
     */
    Question updateQuestion(Long questionId, QuestionCreateRequest request, Long creatorId);
    
    /**
     * 删除题目
     */
    boolean deleteQuestion(Long questionId, Long creatorId);
    
    /**
     * 根据ID查询题目
     */
    Question findQuestionById(Long questionId);
    
    /**
     * 分页查询题目
     */
    IPage<QuestionVO> getQuestionPage(Long courseId, String type, String difficulty, 
                                    String keyword, Integer page, Integer size);
    
    /**
     * 根据课程查询题目
     */
    List<QuestionVO> getQuestionsByCourse(Long courseId);
    
    /**
     * 随机获取题目
     */
    List<QuestionVO> getRandomQuestions(Long courseId, String type, String difficulty, Integer limit);
    
    // ==================== 试卷管理 ====================
    
    /**
     * 创建试卷
     */
    ExamPaper createExamPaper(ExamPaperCreateRequest request, Long creatorId);
    
    /**
     * 更新试卷
     */
    ExamPaper updateExamPaper(Long paperId, ExamPaperCreateRequest request, Long creatorId);
    
    /**
     * 删除试卷
     */
    boolean deleteExamPaper(Long paperId, Long creatorId);
    
    /**
     * 根据ID查询试卷
     */
    ExamPaper findExamPaperById(Long paperId);
    
    /**
     * 查询试卷详情
     */
    ExamPaperVO getExamPaperDetail(Long paperId);
    
    /**
     * 分页查询试卷
     */
    IPage<ExamPaperVO> getExamPaperPage(Long courseId, String type, String status,
                                      String keyword, Integer page, Integer size);
    
    /**
     * 根据课程查询试卷
     */
    List<ExamPaperVO> getExamPapersByCourse(Long courseId, String status);
    
    /**
     * 发布试卷
     */
    boolean publishExamPaper(Long paperId, Long creatorId);
    
    /**
     * 归档试卷
     */
    boolean archiveExamPaper(Long paperId, Long creatorId);
    
    // ==================== 考试管理 ====================
    
    /**
     * 开始考试
     */
    ExamRecord startExam(Long paperId, Long userId);
    
    /**
     * 提交考试
     */
    ExamRecord submitExam(ExamSubmitRequest request, Long userId);
    
    /**
     * 自动阅卷
     */
    ExamRecord autoGrade(Long recordId);
    
    /**
     * 手动阅卷
     */
    ExamRecord manualGrade(Long recordId, List<ExamSubmitRequest.AnswerItem> answers, Long graderId);
    
    /**
     * 获取考试记录
     */
    ExamRecord getExamRecord(Long recordId);
    
    /**
     * 查询考试记录详情
     */
    ExamRecordVO getExamRecordDetail(Long recordId);
    
    /**
     * 分页查询考试记录
     */
    IPage<ExamRecordVO> getExamRecordPage(Long paperId, Long userId, String status,
                                        Integer page, Integer size);
    
    /**
     * 获取用户最近考试记录
     */
    List<ExamRecordVO> getRecentExamRecords(Long userId, Integer limit);
    
    /**
     * 检查考试权限
     */
    boolean checkExamPermission(Long paperId, Long userId);
    
    /**
     * 统计考试数据
     */
    void updateExamStats(Long paperId);
}
