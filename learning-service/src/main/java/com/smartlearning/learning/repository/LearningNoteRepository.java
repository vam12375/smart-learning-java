package com.smartlearning.learning.repository;

import com.smartlearning.learning.document.LearningNoteDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 学习笔记MongoDB Repository
 */
@Repository
public interface LearningNoteRepository extends MongoRepository<LearningNoteDocument, String> {
    
    /**
     * 根据用户ID查询笔记
     */
    Page<LearningNoteDocument> findByUserIdAndDeletedFalseOrderByCreateTimeDesc(Long userId, Pageable pageable);
    
    /**
     * 根据用户ID和课程ID查询笔记
     */
    List<LearningNoteDocument> findByUserIdAndCourseIdAndDeletedFalseOrderByCreateTimeDesc(Long userId, Long courseId);
    
    /**
     * 根据用户ID和课时ID查询笔记
     */
    List<LearningNoteDocument> findByUserIdAndLessonIdAndDeletedFalseOrderByCreateTimeDesc(Long userId, Long lessonId);
    
    /**
     * 查询公开笔记
     */
    Page<LearningNoteDocument> findByCourseIdAndIsPublicTrueAndDeletedFalseOrderByLikeCountDescCreateTimeDesc(Long courseId, Pageable pageable);
    
    /**
     * 根据标签查询笔记
     */
    @Query("{'tags': {$in: ?0}, 'isPublic': true, 'deleted': false}")
    Page<LearningNoteDocument> findByTagsInAndIsPublicTrueAndDeletedFalse(List<String> tags, Pageable pageable);
    
    /**
     * 统计用户笔记数量
     */
    long countByUserIdAndDeletedFalse(Long userId);
    
    /**
     * 统计课程笔记数量
     */
    long countByCourseIdAndIsPublicTrueAndDeletedFalse(Long courseId);
    
    /**
     * 查询热门笔记（按点赞数排序）
     */
    @Query("{'isPublic': true, 'deleted': false, 'createTime': {$gte: ?0}}")
    Page<LearningNoteDocument> findPopularNotes(LocalDateTime startTime, Pageable pageable);
    
    /**
     * 全文搜索笔记
     */
    @Query("{'$or': [{'title': {$regex: ?0, $options: 'i'}}, {'content': {$regex: ?0, $options: 'i'}}], 'isPublic': true, 'deleted': false}")
    Page<LearningNoteDocument> searchNotes(String keyword, Pageable pageable);
}
