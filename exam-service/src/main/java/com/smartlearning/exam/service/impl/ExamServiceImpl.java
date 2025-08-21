package com.smartlearning.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartlearning.common.constant.CommonConstants;
import com.smartlearning.common.exception.BusinessException;
import com.smartlearning.common.result.ResultCode;
import com.smartlearning.exam.dto.ExamPaperCreateRequest;
import com.smartlearning.exam.dto.ExamSubmitRequest;
import com.smartlearning.exam.dto.QuestionCreateRequest;
import com.smartlearning.exam.entity.*;
import com.smartlearning.exam.mapper.*;
import com.smartlearning.exam.service.ExamService;
import com.smartlearning.exam.vo.ExamPaperVO;
import com.smartlearning.exam.vo.ExamRecordVO;
import com.smartlearning.exam.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 考试服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final QuestionMapper questionMapper;
    private final ExamPaperMapper examPaperMapper;
    private final PaperQuestionMapper paperQuestionMapper;
    private final ExamRecordMapper examRecordMapper;
    private final ExamAnswerMapper examAnswerMapper;

    // ==================== 题目管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Question createQuestion(QuestionCreateRequest request, Long creatorId) {
        log.info("创建题目请求: type={}, creatorId={}", request.getType(), creatorId);

        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setCreatorId(creatorId);
        question.setStatus(1);
        question.setUseCount(0);
        question.setAccuracy(0.0);
        question.setCreateTime(LocalDateTime.now());
        question.setUpdateTime(LocalDateTime.now());

        // 处理选项
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            question.setOptions(String.join("||", request.getOptions()));
        }

        // 处理标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            question.setTags(String.join(",", request.getTags()));
        }

        int result = questionMapper.insert(question);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "题目创建失败");
        }

        log.info("题目创建成功: questionId={}", question.getId());
        return question;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Question updateQuestion(Long questionId, QuestionCreateRequest request, Long creatorId) {
        log.info("更新题目请求: questionId={}, creatorId={}", questionId, creatorId);

        Question existingQuestion = findQuestionById(questionId);
        if (existingQuestion == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "题目不存在");
        }

        if (!existingQuestion.getCreatorId().equals(creatorId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此题目");
        }

        Question question = new Question();
        BeanUtils.copyProperties(request, question);
        question.setId(questionId);
        question.setUpdateTime(LocalDateTime.now());

        // 处理选项
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            question.setOptions(String.join("||", request.getOptions()));
        }

        // 处理标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            question.setTags(String.join(",", request.getTags()));
        }

        int result = questionMapper.updateById(question);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "题目更新失败");
        }

        log.info("题目更新成功: questionId={}", questionId);
        return findQuestionById(questionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteQuestion(Long questionId, Long creatorId) {
        log.info("删除题目请求: questionId={}, creatorId={}", questionId, creatorId);

        Question question = findQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "题目不存在");
        }

        if (!question.getCreatorId().equals(creatorId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此题目");
        }

        int result = questionMapper.deleteById(questionId);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "题目删除失败");
        }

        log.info("题目删除成功: questionId={}", questionId);
        return true;
    }

    @Override
    public Question findQuestionById(Long questionId) {
        return questionMapper.selectById(questionId);
    }

    @Override
    public IPage<QuestionVO> getQuestionPage(Long courseId, String type, String difficulty,
            String keyword, Integer page, Integer size) {
        Page<QuestionVO> pageParam = new Page<>(page, size);
        return questionMapper.selectQuestionPage(pageParam, courseId, type, difficulty, keyword);
    }

    @Override
    public List<QuestionVO> getQuestionsByCourse(Long courseId) {
        return questionMapper.selectByCourse(courseId);
    }

    @Override
    public List<QuestionVO> getRandomQuestions(Long courseId, String type, String difficulty, Integer limit) {
        return questionMapper.selectRandomQuestions(courseId, type, difficulty, limit);
    }

    // ==================== 试卷管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamPaper createExamPaper(ExamPaperCreateRequest request, Long creatorId) {
        log.info("创建试卷请求: title={}, creatorId={}", request.getTitle(), creatorId);

        ExamPaper paper = new ExamPaper();
        BeanUtils.copyProperties(request, paper);
        paper.setCreatorId(creatorId);
        paper.setStatus(CommonConstants.ExamStatus.DRAFT);
        paper.setQuestionCount(0);
        paper.setParticipantCount(0);
        paper.setAverageScore(0.0);
        paper.setPassRate(0.0);
        paper.setCreateTime(LocalDateTime.now());
        paper.setUpdateTime(LocalDateTime.now());

        int result = examPaperMapper.insert(paper);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "试卷创建失败");
        }

        // 添加题目
        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            addQuestionsToExamPaper(paper.getId(), request.getQuestions());
        }

        log.info("试卷创建成功: paperId={}", paper.getId());
        return paper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamPaper updateExamPaper(Long paperId, ExamPaperCreateRequest request, Long creatorId) {
        log.info("更新试卷请求: paperId={}, creatorId={}", paperId, creatorId);

        ExamPaper existingPaper = findExamPaperById(paperId);
        if (existingPaper == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "试卷不存在");
        }

        if (!existingPaper.getCreatorId().equals(creatorId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此试卷");
        }

        ExamPaper paper = new ExamPaper();
        BeanUtils.copyProperties(request, paper);
        paper.setId(paperId);
        paper.setUpdateTime(LocalDateTime.now());

        int result = examPaperMapper.updateById(paper);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "试卷更新失败");
        }

        // 更新题目
        if (request.getQuestions() != null) {
            paperQuestionMapper.deleteByPaperId(paperId);
            if (!request.getQuestions().isEmpty()) {
                addQuestionsToExamPaper(paperId, request.getQuestions());
            }
        }

        log.info("试卷更新成功: paperId={}", paperId);
        return findExamPaperById(paperId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteExamPaper(Long paperId, Long creatorId) {
        log.info("删除试卷请求: paperId={}, creatorId={}", paperId, creatorId);

        ExamPaper paper = findExamPaperById(paperId);
        if (paper == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "试卷不存在");
        }

        if (!paper.getCreatorId().equals(creatorId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此试卷");
        }

        // 检查是否有考试记录
        Long recordCount = examRecordMapper.countRecords(paperId, null, null);
        if (recordCount > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "试卷已有考试记录，不能删除");
        }

        // 删除试卷题目关联
        paperQuestionMapper.deleteByPaperId(paperId);

        // 删除试卷
        int result = examPaperMapper.deleteById(paperId);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "试卷删除失败");
        }

        log.info("试卷删除成功: paperId={}", paperId);
        return true;
    }

    @Override
    public ExamPaper findExamPaperById(Long paperId) {
        return examPaperMapper.selectById(paperId);
    }

    @Override
    public ExamPaperVO getExamPaperDetail(Long paperId) {
        return examPaperMapper.selectPaperDetail(paperId);
    }

    @Override
    public IPage<ExamPaperVO> getExamPaperPage(Long courseId, String type, String status,
            String keyword, Integer page, Integer size) {
        Page<ExamPaperVO> pageParam = new Page<>(page, size);
        return examPaperMapper.selectPaperPage(pageParam, courseId, type, status, keyword);
    }

    @Override
    public List<ExamPaperVO> getExamPapersByCourse(Long courseId, String status) {
        return examPaperMapper.selectByCourse(courseId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishExamPaper(Long paperId, Long creatorId) {
        log.info("发布试卷请求: paperId={}, creatorId={}", paperId, creatorId);

        ExamPaper paper = findExamPaperById(paperId);
        if (paper == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "试卷不存在");
        }

        if (!paper.getCreatorId().equals(creatorId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此试卷");
        }

        // 检查试卷是否有题目
        Integer questionCount = paperQuestionMapper.countByPaperId(paperId);
        if (questionCount == null || questionCount == 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "试卷没有题目，不能发布");
        }

        paper.setStatus(CommonConstants.ExamStatus.PUBLISHED);
        paper.setUpdateTime(LocalDateTime.now());

        int result = examPaperMapper.updateById(paper);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "试卷发布失败");
        }

        log.info("试卷发布成功: paperId={}", paperId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean archiveExamPaper(Long paperId, Long creatorId) {
        log.info("归档试卷请求: paperId={}, creatorId={}", paperId, creatorId);

        ExamPaper paper = findExamPaperById(paperId);
        if (paper == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "试卷不存在");
        }

        if (!paper.getCreatorId().equals(creatorId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此试卷");
        }

        paper.setStatus(CommonConstants.ExamStatus.ARCHIVED);
        paper.setUpdateTime(LocalDateTime.now());

        int result = examPaperMapper.updateById(paper);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "试卷归档失败");
        }

        log.info("试卷归档成功: paperId={}", paperId);
        return true;
    }

    // ==================== 考试管理 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamRecord startExam(Long paperId, Long userId) {
        log.info("开始考试请求: paperId={}, userId={}", paperId, userId);

        // 检查考试权限
        if (!checkExamPermission(paperId, userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限参加此考试");
        }

        ExamPaper paper = findExamPaperById(paperId);
        if (paper == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "试卷不存在");
        }

        if (!CommonConstants.ExamStatus.PUBLISHED.equals(paper.getStatus())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "试卷未发布");
        }

        // 检查考试时间
        LocalDateTime now = LocalDateTime.now();
        if (paper.getStartTime() != null && now.isBefore(paper.getStartTime())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "考试尚未开始");
        }
        if (paper.getEndTime() != null && now.isAfter(paper.getEndTime())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "考试已结束");
        }

        // 检查重考次数
        List<ExamRecord> existingRecords = examRecordMapper.selectByUserAndPaper(userId, paperId);
        if (paper.getRetakeLimit() != null && paper.getRetakeLimit() >= 0) {
            long submittedCount = existingRecords.stream()
                    .filter(record -> CommonConstants.ExamRecordStatus.SUBMITTED.equals(record.getStatus()) ||
                            CommonConstants.ExamRecordStatus.GRADED.equals(record.getStatus()))
                    .count();
            if (submittedCount >= paper.getRetakeLimit()) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "已达到最大重考次数");
            }
        }

        // 检查是否有进行中的考试
        boolean hasOngoingExam = existingRecords.stream()
                .anyMatch(record -> CommonConstants.ExamRecordStatus.STARTED.equals(record.getStatus()));
        if (hasOngoingExam) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "已有进行中的考试");
        }

        // 创建考试记录
        ExamRecord record = new ExamRecord();
        record.setPaperId(paperId);
        record.setUserId(userId);
        record.setStatus(CommonConstants.ExamRecordStatus.STARTED);
        record.setTotalScore(paper.getTotalScore());
        record.setScore(0);
        record.setCorrectCount(0);
        record.setWrongCount(0);
        record.setDuration(0);
        record.setIsPassed(0);
        record.setStartTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        int result = examRecordMapper.insert(record);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "考试开始失败");
        }

        // 增加参考人数
        examPaperMapper.incrementParticipantCount(paperId);

        log.info("考试开始成功: recordId={}", record.getId());
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamRecord submitExam(ExamSubmitRequest request, Long userId) {
        log.info("提交考试请求: recordId={}, userId={}", request.getRecordId(), userId);

        ExamRecord record = examRecordMapper.selectById(request.getRecordId());
        if (record == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "考试记录不存在");
        }

        if (!record.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限操作此考试记录");
        }

        if (!CommonConstants.ExamRecordStatus.STARTED.equals(record.getStatus())) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "考试状态不正确");
        }

        // 保存答题记录
        if (request.getAnswers() != null && !request.getAnswers().isEmpty()) {
            saveExamAnswers(request.getRecordId(), request.getAnswers());
        }

        // 更新考试记录状态
        record.setStatus(CommonConstants.ExamRecordStatus.SUBMITTED);
        record.setSubmitTime(LocalDateTime.now());
        record.setDuration((int) java.time.Duration.between(record.getStartTime(), LocalDateTime.now()).getSeconds());
        record.setUpdateTime(LocalDateTime.now());

        int result = examRecordMapper.updateById(record);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "考试提交失败");
        }

        // 自动阅卷
        autoGrade(request.getRecordId());

        log.info("考试提交成功: recordId={}", request.getRecordId());
        return examRecordMapper.selectById(request.getRecordId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamRecord autoGrade(Long recordId) {
        log.info("自动阅卷请求: recordId={}", recordId);

        ExamRecord record = examRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "考试记录不存在");
        }

        // 获取答题记录
        List<ExamAnswer> answers = examAnswerMapper.selectByRecordId(recordId);

        // 自动阅卷
        for (ExamAnswer answer : answers) {
            Question question = questionMapper.selectById(answer.getQuestionId());
            if (question != null) {
                // 判断答案是否正确
                boolean isCorrect = checkAnswer(question, answer.getUserAnswer());
                answer.setIsCorrect(isCorrect ? 1 : 0);
                answer.setScore(isCorrect ? answer.getTotalScore() : 0);
                answer.setCorrectAnswer(question.getAnswer());
                answer.setUpdateTime(LocalDateTime.now());

                examAnswerMapper.updateById(answer);
            }
        }

        // 统计成绩
        Integer totalScore = examAnswerMapper.sumScore(recordId);
        Integer correctCount = examAnswerMapper.countCorrectAnswers(recordId);
        Integer wrongCount = examAnswerMapper.countWrongAnswers(recordId);

        // 判断是否通过
        ExamPaper paper = examPaperMapper.selectById(record.getPaperId());
        boolean isPassed = totalScore != null && paper != null &&
                totalScore >= paper.getPassScore();

        // 更新考试记录
        record.setStatus(CommonConstants.ExamRecordStatus.GRADED);
        record.setScore(totalScore != null ? totalScore : 0);
        record.setCorrectCount(correctCount != null ? correctCount : 0);
        record.setWrongCount(wrongCount != null ? wrongCount : 0);
        record.setIsPassed(isPassed ? 1 : 0);
        record.setGradeTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        examRecordMapper.updateById(record);

        // 更新试卷统计
        updateExamStats(record.getPaperId());

        log.info("自动阅卷完成: recordId={}, score={}", recordId, totalScore);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExamRecord manualGrade(Long recordId, List<ExamSubmitRequest.AnswerItem> answers, Long graderId) {
        log.info("手动阅卷请求: recordId={}, graderId={}", recordId, graderId);

        ExamRecord record = examRecordMapper.selectById(recordId);
        if (record == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "考试记录不存在");
        }

        // 更新答题记录
        if (answers != null && !answers.isEmpty()) {
            List<ExamAnswer> examAnswers = examAnswerMapper.selectByRecordId(recordId);
            for (ExamAnswer examAnswer : examAnswers) {
                answers.stream()
                        .filter(item -> item.getQuestionId().equals(examAnswer.getQuestionId()))
                        .findFirst()
                        .ifPresent(item -> {
                            examAnswer.setUserAnswer(item.getUserAnswer());
                            examAnswer.setUpdateTime(LocalDateTime.now());
                        });
            }
            examAnswerMapper.batchUpdate(examAnswers);
        }

        // 重新自动阅卷
        autoGrade(recordId);

        // 更新阅卷者
        record = examRecordMapper.selectById(recordId);
        record.setGraderId(graderId);
        record.setUpdateTime(LocalDateTime.now());
        examRecordMapper.updateById(record);

        log.info("手动阅卷完成: recordId={}", recordId);
        return record;
    }

    @Override
    public ExamRecord getExamRecord(Long recordId) {
        return examRecordMapper.selectById(recordId);
    }

    @Override
    public ExamRecordVO getExamRecordDetail(Long recordId) {
        return examRecordMapper.selectRecordDetail(recordId);
    }

    @Override
    public IPage<ExamRecordVO> getExamRecordPage(Long paperId, Long userId, String status,
            Integer page, Integer size) {
        Page<ExamRecordVO> pageParam = new Page<>(page, size);
        return examRecordMapper.selectRecordPage(pageParam, paperId, userId, status);
    }

    @Override
    public List<ExamRecordVO> getRecentExamRecords(Long userId, Integer limit) {
        return examRecordMapper.selectRecentRecords(userId, limit);
    }

    @Override
    public boolean checkExamPermission(Long paperId, Long userId) {
        // 这里可以根据业务需求实现权限检查逻辑
        // 例如：检查用户是否已购买课程、是否在考试时间范围内等
        return true;
    }

    @Override
    public void updateExamStats(Long paperId) {
        try {
            Double averageScore = examRecordMapper.calculateAverageScore(paperId);
            ExamPaper paper = examPaperMapper.selectById(paperId);
            Double passRate = null;
            if (paper != null) {
                passRate = examRecordMapper.calculatePassRate(paperId, paper.getPassScore());
            }

            Long participantCount = examRecordMapper.countRecords(paperId, null,
                    CommonConstants.ExamRecordStatus.GRADED);

            examPaperMapper.updatePaperStats(paperId,
                    participantCount != null ? participantCount.intValue() : 0,
                    averageScore, passRate);

            // 更新排名
            examRecordMapper.updateRanking(paperId);
        } catch (Exception e) {
            log.error("更新考试统计失败: paperId={}", paperId, e);
        }
    }

    /**
     * 保存答题记录
     */
    private void saveExamAnswers(Long recordId, List<ExamSubmitRequest.AnswerItem> answers) {
        List<ExamAnswer> examAnswers = new ArrayList<>();

        for (ExamSubmitRequest.AnswerItem item : answers) {
            ExamAnswer answer = new ExamAnswer();
            answer.setRecordId(recordId);
            answer.setQuestionId(item.getQuestionId());
            answer.setUserAnswer(item.getUserAnswer());
            answer.setDuration(item.getDuration());
            answer.setCreateTime(LocalDateTime.now());
            answer.setUpdateTime(LocalDateTime.now());
            examAnswers.add(answer);
        }

        if (!examAnswers.isEmpty()) {
            examAnswerMapper.batchInsert(examAnswers);
        }
    }

    /**
     * 检查答案是否正确
     */
    private boolean checkAnswer(Question question, String userAnswer) {
        if (question == null || userAnswer == null) {
            return false;
        }

        String correctAnswer = question.getAnswer();
        if (correctAnswer == null) {
            return false;
        }

        // 根据题目类型进行不同的判断
        switch (question.getType()) {
            case "SINGLE":
            case "JUDGE":
                return correctAnswer.trim().equalsIgnoreCase(userAnswer.trim());
            case "MULTIPLE":
                // 多选题需要排序后比较
                String[] correctOptions = correctAnswer.split(",");
                String[] userOptions = userAnswer.split(",");
                java.util.Arrays.sort(correctOptions);
                java.util.Arrays.sort(userOptions);
                return java.util.Arrays.equals(correctOptions, userOptions);
            case "FILL":
                // 填空题可以有多个正确答案，用|分隔
                String[] correctAnswers = correctAnswer.split("\\|");
                for (String answer : correctAnswers) {
                    if (answer.trim().equalsIgnoreCase(userAnswer.trim())) {
                        return true;
                    }
                }
                return false;
            case "ESSAY":
                // 问答题需要人工阅卷，这里返回false
                return false;
            default:
                return false;
        }
    }

    /**
     * 添加题目到试卷
     */
    private void addQuestionsToExamPaper(Long paperId, List<ExamPaperCreateRequest.QuestionItem> questions) {
        List<PaperQuestion> paperQuestions = new ArrayList<>();

        for (ExamPaperCreateRequest.QuestionItem item : questions) {
            PaperQuestion paperQuestion = new PaperQuestion();
            paperQuestion.setPaperId(paperId);
            paperQuestion.setQuestionId(item.getQuestionId());
            paperQuestion.setQuestionOrder(item.getOrder());
            paperQuestion.setScore(item.getScore());
            paperQuestion.setCreateTime(LocalDateTime.now());
            paperQuestions.add(paperQuestion);
        }

        if (!paperQuestions.isEmpty()) {
            paperQuestionMapper.batchInsert(paperQuestions);

            // 更新试卷题目数量和总分
            Integer questionCount = paperQuestions.size();
            Integer totalScore = paperQuestions.stream()
                    .mapToInt(PaperQuestion::getScore)
                    .sum();

            ExamPaper paper = new ExamPaper();
            paper.setId(paperId);
            paper.setQuestionCount(questionCount);
            paper.setTotalScore(totalScore);
            paper.setUpdateTime(LocalDateTime.now());
            examPaperMapper.updateById(paper);
        }
    }
}
