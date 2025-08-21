package com.smartlearning.exam.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smartlearning.common.result.Result;
import com.smartlearning.exam.dto.ExamPaperCreateRequest;
import com.smartlearning.exam.dto.ExamSubmitRequest;
import com.smartlearning.exam.dto.QuestionCreateRequest;
import com.smartlearning.exam.entity.ExamPaper;
import com.smartlearning.exam.entity.ExamRecord;
import com.smartlearning.exam.entity.Question;
import com.smartlearning.exam.service.ExamService;
import com.smartlearning.exam.vo.ExamPaperVO;
import com.smartlearning.exam.vo.ExamRecordVO;
import com.smartlearning.exam.vo.QuestionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考试控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/exam")
@RequiredArgsConstructor
@Tag(name = "考试管理", description = "题目、试卷、考试相关接口")
public class ExamController {
    
    private final ExamService examService;
    
    // ==================== 题目管理 ====================
    
    /**
     * 创建题目
     */
    @PostMapping("/questions")
    @Operation(summary = "创建题目", description = "教师创建新题目")
    public Result<Question> createQuestion(@Valid @RequestBody QuestionCreateRequest request,
                                         @RequestHeader("X-User-Id") Long creatorId) {
        log.info("创建题目请求: type={}, creatorId={}", request.getType(), creatorId);
        Question question = examService.createQuestion(request, creatorId);
        return Result.success("题目创建成功", question);
    }
    
    /**
     * 更新题目
     */
    @PutMapping("/questions/{questionId}")
    @Operation(summary = "更新题目", description = "教师更新题目信息")
    public Result<Question> updateQuestion(@Parameter(description = "题目ID") @PathVariable Long questionId,
                                         @Valid @RequestBody QuestionCreateRequest request,
                                         @RequestHeader("X-User-Id") Long creatorId) {
        log.info("更新题目请求: questionId={}, creatorId={}", questionId, creatorId);
        Question question = examService.updateQuestion(questionId, request, creatorId);
        return Result.success("题目更新成功", question);
    }
    
    /**
     * 删除题目
     */
    @DeleteMapping("/questions/{questionId}")
    @Operation(summary = "删除题目", description = "教师删除题目")
    public Result<Void> deleteQuestion(@Parameter(description = "题目ID") @PathVariable Long questionId,
                                     @RequestHeader("X-User-Id") Long creatorId) {
        log.info("删除题目请求: questionId={}, creatorId={}", questionId, creatorId);
        examService.deleteQuestion(questionId, creatorId);
        return Result.success("题目删除成功");
    }
    
    /**
     * 获取题目详情
     */
    @GetMapping("/questions/{questionId}")
    @Operation(summary = "获取题目详情", description = "根据ID获取题目详细信息")
    public Result<Question> getQuestionById(@Parameter(description = "题目ID") @PathVariable Long questionId) {
        log.info("获取题目详情请求: questionId={}", questionId);
        Question question = examService.findQuestionById(questionId);
        return Result.success("获取成功", question);
    }
    
    /**
     * 分页查询题目
     */
    @GetMapping("/questions")
    @Operation(summary = "分页查询题目", description = "根据条件分页查询题目列表")
    public Result<IPage<QuestionVO>> getQuestionPage(@Parameter(description = "课程ID") @RequestParam(required = false) Long courseId,
                                                   @Parameter(description = "题目类型") @RequestParam(required = false) String type,
                                                   @Parameter(description = "题目难度") @RequestParam(required = false) String difficulty,
                                                   @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
                                                   @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                                   @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size) {
        log.info("分页查询题目请求: courseId={}, type={}, difficulty={}", courseId, type, difficulty);
        IPage<QuestionVO> result = examService.getQuestionPage(courseId, type, difficulty, keyword, page, size);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据课程查询题目
     */
    @GetMapping("/questions/course/{courseId}")
    @Operation(summary = "根据课程查询题目", description = "获取指定课程的题目列表")
    public Result<List<QuestionVO>> getQuestionsByCourse(@Parameter(description = "课程ID") @PathVariable Long courseId) {
        log.info("根据课程查询题目请求: courseId={}", courseId);
        List<QuestionVO> questions = examService.getQuestionsByCourse(courseId);
        return Result.success("查询成功", questions);
    }
    
    /**
     * 随机获取题目
     */
    @GetMapping("/questions/random")
    @Operation(summary = "随机获取题目", description = "根据条件随机获取题目")
    public Result<List<QuestionVO>> getRandomQuestions(@Parameter(description = "课程ID") @RequestParam Long courseId,
                                                     @Parameter(description = "题目类型") @RequestParam(required = false) String type,
                                                     @Parameter(description = "题目难度") @RequestParam(required = false) String difficulty,
                                                     @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("随机获取题目请求: courseId={}, type={}, limit={}", courseId, type, limit);
        List<QuestionVO> questions = examService.getRandomQuestions(courseId, type, difficulty, limit);
        return Result.success("获取成功", questions);
    }
    
    // ==================== 试卷管理 ====================
    
    /**
     * 创建试卷
     */
    @PostMapping("/papers")
    @Operation(summary = "创建试卷", description = "教师创建新试卷")
    public Result<ExamPaper> createExamPaper(@Valid @RequestBody ExamPaperCreateRequest request,
                                           @RequestHeader("X-User-Id") Long creatorId) {
        log.info("创建试卷请求: title={}, creatorId={}", request.getTitle(), creatorId);
        ExamPaper paper = examService.createExamPaper(request, creatorId);
        return Result.success("试卷创建成功", paper);
    }
    
    /**
     * 更新试卷
     */
    @PutMapping("/papers/{paperId}")
    @Operation(summary = "更新试卷", description = "教师更新试卷信息")
    public Result<ExamPaper> updateExamPaper(@Parameter(description = "试卷ID") @PathVariable Long paperId,
                                           @Valid @RequestBody ExamPaperCreateRequest request,
                                           @RequestHeader("X-User-Id") Long creatorId) {
        log.info("更新试卷请求: paperId={}, creatorId={}", paperId, creatorId);
        ExamPaper paper = examService.updateExamPaper(paperId, request, creatorId);
        return Result.success("试卷更新成功", paper);
    }
    
    /**
     * 删除试卷
     */
    @DeleteMapping("/papers/{paperId}")
    @Operation(summary = "删除试卷", description = "教师删除试卷")
    public Result<Void> deleteExamPaper(@Parameter(description = "试卷ID") @PathVariable Long paperId,
                                       @RequestHeader("X-User-Id") Long creatorId) {
        log.info("删除试卷请求: paperId={}, creatorId={}", paperId, creatorId);
        examService.deleteExamPaper(paperId, creatorId);
        return Result.success("试卷删除成功");
    }
    
    /**
     * 获取试卷详情
     */
    @GetMapping("/papers/{paperId}")
    @Operation(summary = "获取试卷详情", description = "根据ID获取试卷详细信息")
    public Result<ExamPaperVO> getExamPaperDetail(@Parameter(description = "试卷ID") @PathVariable Long paperId) {
        log.info("获取试卷详情请求: paperId={}", paperId);
        ExamPaperVO paper = examService.getExamPaperDetail(paperId);
        return Result.success("获取成功", paper);
    }
    
    /**
     * 分页查询试卷
     */
    @GetMapping("/papers")
    @Operation(summary = "分页查询试卷", description = "根据条件分页查询试卷列表")
    public Result<IPage<ExamPaperVO>> getExamPaperPage(@Parameter(description = "课程ID") @RequestParam(required = false) Long courseId,
                                                      @Parameter(description = "试卷类型") @RequestParam(required = false) String type,
                                                      @Parameter(description = "试卷状态") @RequestParam(required = false) String status,
                                                      @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
                                                      @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                                      @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size) {
        log.info("分页查询试卷请求: courseId={}, type={}, status={}", courseId, type, status);
        IPage<ExamPaperVO> result = examService.getExamPaperPage(courseId, type, status, keyword, page, size);
        return Result.success("查询成功", result);
    }
    
    /**
     * 根据课程查询试卷
     */
    @GetMapping("/papers/course/{courseId}")
    @Operation(summary = "根据课程查询试卷", description = "获取指定课程的试卷列表")
    public Result<List<ExamPaperVO>> getExamPapersByCourse(@Parameter(description = "课程ID") @PathVariable Long courseId,
                                                          @Parameter(description = "试卷状态") @RequestParam(required = false) String status) {
        log.info("根据课程查询试卷请求: courseId={}, status={}", courseId, status);
        List<ExamPaperVO> papers = examService.getExamPapersByCourse(courseId, status);
        return Result.success("查询成功", papers);
    }
    
    /**
     * 发布试卷
     */
    @PostMapping("/papers/{paperId}/publish")
    @Operation(summary = "发布试卷", description = "教师发布试卷")
    public Result<Void> publishExamPaper(@Parameter(description = "试卷ID") @PathVariable Long paperId,
                                        @RequestHeader("X-User-Id") Long creatorId) {
        log.info("发布试卷请求: paperId={}, creatorId={}", paperId, creatorId);
        examService.publishExamPaper(paperId, creatorId);
        return Result.success("试卷发布成功");
    }
    
    /**
     * 归档试卷
     */
    @PostMapping("/papers/{paperId}/archive")
    @Operation(summary = "归档试卷", description = "教师归档试卷")
    public Result<Void> archiveExamPaper(@Parameter(description = "试卷ID") @PathVariable Long paperId,
                                        @RequestHeader("X-User-Id") Long creatorId) {
        log.info("归档试卷请求: paperId={}, creatorId={}", paperId, creatorId);
        examService.archiveExamPaper(paperId, creatorId);
        return Result.success("试卷归档成功");
    }
    
    // ==================== 考试管理 ====================
    
    /**
     * 开始考试
     */
    @PostMapping("/records/start/{paperId}")
    @Operation(summary = "开始考试", description = "学生开始考试")
    public Result<ExamRecord> startExam(@Parameter(description = "试卷ID") @PathVariable Long paperId,
                                      @RequestHeader("X-User-Id") Long userId) {
        log.info("开始考试请求: paperId={}, userId={}", paperId, userId);
        ExamRecord record = examService.startExam(paperId, userId);
        return Result.success("考试开始成功", record);
    }
    
    /**
     * 提交考试
     */
    @PostMapping("/records/submit")
    @Operation(summary = "提交考试", description = "学生提交考试答案")
    public Result<ExamRecord> submitExam(@Valid @RequestBody ExamSubmitRequest request,
                                       @RequestHeader("X-User-Id") Long userId) {
        log.info("提交考试请求: recordId={}, userId={}", request.getRecordId(), userId);
        ExamRecord record = examService.submitExam(request, userId);
        return Result.success("考试提交成功", record);
    }
    
    /**
     * 获取考试记录详情
     */
    @GetMapping("/records/{recordId}")
    @Operation(summary = "获取考试记录详情", description = "根据ID获取考试记录详细信息")
    public Result<ExamRecordVO> getExamRecordDetail(@Parameter(description = "考试记录ID") @PathVariable Long recordId) {
        log.info("获取考试记录详情请求: recordId={}", recordId);
        ExamRecordVO record = examService.getExamRecordDetail(recordId);
        return Result.success("获取成功", record);
    }
    
    /**
     * 分页查询考试记录
     */
    @GetMapping("/records")
    @Operation(summary = "分页查询考试记录", description = "根据条件分页查询考试记录")
    public Result<IPage<ExamRecordVO>> getExamRecordPage(@Parameter(description = "试卷ID") @RequestParam(required = false) Long paperId,
                                                       @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
                                                       @Parameter(description = "考试状态") @RequestParam(required = false) String status,
                                                       @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
                                                       @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") Integer size) {
        log.info("分页查询考试记录请求: paperId={}, userId={}, status={}", paperId, userId, status);
        IPage<ExamRecordVO> result = examService.getExamRecordPage(paperId, userId, status, page, size);
        return Result.success("查询成功", result);
    }
    
    /**
     * 获取用户最近考试记录
     */
    @GetMapping("/records/recent")
    @Operation(summary = "获取最近考试记录", description = "获取用户最近的考试记录")
    public Result<List<ExamRecordVO>> getRecentExamRecords(@RequestHeader("X-User-Id") Long userId,
                                                         @Parameter(description = "数量限制") @RequestParam(defaultValue = "10") Integer limit) {
        log.info("获取最近考试记录请求: userId={}, limit={}", userId, limit);
        List<ExamRecordVO> records = examService.getRecentExamRecords(userId, limit);
        return Result.success("获取成功", records);
    }
    
    /**
     * 手动阅卷
     */
    @PostMapping("/records/{recordId}/grade")
    @Operation(summary = "手动阅卷", description = "教师手动阅卷")
    public Result<ExamRecord> manualGrade(@Parameter(description = "考试记录ID") @PathVariable Long recordId,
                                        @RequestBody List<ExamSubmitRequest.AnswerItem> answers,
                                        @RequestHeader("X-User-Id") Long graderId) {
        log.info("手动阅卷请求: recordId={}, graderId={}", recordId, graderId);
        ExamRecord record = examService.manualGrade(recordId, answers, graderId);
        return Result.success("阅卷完成", record);
    }
}
