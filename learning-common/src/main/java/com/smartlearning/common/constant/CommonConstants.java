package com.smartlearning.common.constant;

/**
 * 公共常量类
 */
public class CommonConstants {

    /**
     * 成功标识
     */
    public static final String SUCCESS = "success";

    /**
     * 失败标识
     */
    public static final String FAIL = "fail";

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE = 1;

    /**
     * 默认每页大小
     */
    public static final int DEFAULT_SIZE = 20;

    /**
     * 最大每页大小
     */
    public static final int MAX_SIZE = 100;

    /**
     * UTF-8编码
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 默认头像
     */
    public static final String DEFAULT_AVATAR = "/avatars/default.jpg";

    /**
     * 用户角色
     */
    public static class UserRole {
        public static final String STUDENT = "STUDENT";
        public static final String TEACHER = "TEACHER";
        public static final String ADMIN = "ADMIN";
    }

    /**
     * 用户状态
     */
    public static class UserStatus {
        public static final int DISABLED = 0;
        public static final int ENABLED = 1;
    }

    /**
     * 课程状态
     */
    public static class CourseStatus {
        public static final String DRAFT = "DRAFT";
        public static final String REVIEWING = "REVIEWING";
        public static final String PUBLISHED = "PUBLISHED";
        public static final String OFFLINE = "OFFLINE";
    }

    /**
     * 课程难度
     */
    public static class CourseDifficulty {
        public static final String BEGINNER = "BEGINNER";
        public static final String INTERMEDIATE = "INTERMEDIATE";
        public static final String ADVANCED = "ADVANCED";
    }

    /**
     * 考试状态
     */
    public static class ExamStatus {
        public static final String DRAFT = "DRAFT";
        public static final String PUBLISHED = "PUBLISHED";
        public static final String ARCHIVED = "ARCHIVED";
    }

    /**
     * 考试记录状态
     */
    public static class ExamRecordStatus {
        public static final String STARTED = "STARTED";
        public static final String SUBMITTED = "SUBMITTED";
        public static final String TIMEOUT = "TIMEOUT";
        public static final String GRADED = "GRADED";
    }

    /**
     * 直播间状态
     */
    public static class LiveRoomStatus {
        public static final String SCHEDULED = "SCHEDULED";
        public static final String LIVE = "LIVE";
        public static final String ENDED = "ENDED";
        public static final String CANCELLED = "CANCELLED";
    }

    /**
     * 缓存键前缀
     */
    public static class CachePrefix {
        public static final String USER_INFO = "user:info:";
        public static final String USER_PROFILE = "user:profile:";
        public static final String USER_STATS = "user:stats:";
        public static final String COURSE_INFO = "course:info:";
        public static final String COURSE_LESSONS = "course:lessons:";
        public static final String LEARNING_PROGRESS = "learning:progress:";
        public static final String HOT_COURSES = "course:hot:list";
        public static final String NEW_COURSES = "course:new:list";
    }

    /**
     * 缓存过期时间（秒）
     */
    public static class CacheExpire {
        public static final long USER_INFO = 30 * 60; // 30分钟
        public static final long USER_PROFILE = 60 * 60; // 1小时
        public static final long USER_STATS = 30 * 60; // 30分钟
        public static final long COURSE_INFO = 60 * 60; // 1小时
        public static final long HOT_DATA = 6 * 60 * 60; // 6小时
        public static final long SEARCH_RESULT = 15 * 60; // 15分钟
    }
}
