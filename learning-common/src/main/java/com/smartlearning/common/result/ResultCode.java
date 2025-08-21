package com.smartlearning.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    
    // 成功
    SUCCESS(200, "操作成功"),
    
    // 客户端错误
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),
    VALIDATION_ERROR(422, "参数校验失败"),
    
    // 服务器错误
    ERROR(500, "系统内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    
    // 业务错误码 (1000-9999)
    // 用户相关 (1000-1999)
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USERNAME_OR_PASSWORD_ERROR(1003, "用户名或密码错误"),
    USER_DISABLED(1004, "用户已被禁用"),
    PASSWORD_ERROR(1005, "密码错误"),
    TOKEN_EXPIRED(1006, "Token已过期"),
    TOKEN_INVALID(1007, "Token无效"),
    
    // 课程相关 (2000-2999)
    COURSE_NOT_FOUND(2001, "课程不存在"),
    COURSE_NOT_PUBLISHED(2002, "课程未发布"),
    COURSE_ALREADY_ENROLLED(2003, "已报名该课程"),
    COURSE_NOT_ENROLLED(2004, "未报名该课程"),
    COURSE_EXPIRED(2005, "课程已过期"),
    
    // 学习相关 (3000-3999)
    LESSON_NOT_FOUND(3001, "课时不存在"),
    LESSON_NOT_ACCESSIBLE(3002, "课时不可访问"),
    LEARNING_RECORD_NOT_FOUND(3003, "学习记录不存在"),
    
    // 考试相关 (4000-4999)
    EXAM_NOT_FOUND(4001, "考试不存在"),
    EXAM_NOT_STARTED(4002, "考试未开始"),
    EXAM_ALREADY_ENDED(4003, "考试已结束"),
    EXAM_ALREADY_SUBMITTED(4004, "考试已提交"),
    EXAM_ATTEMPT_LIMIT_EXCEEDED(4005, "考试次数已用完"),
    
    // 直播相关 (5000-5999)
    LIVE_ROOM_NOT_FOUND(5001, "直播间不存在"),
    LIVE_ROOM_NOT_STARTED(5002, "直播未开始"),
    LIVE_ROOM_ENDED(5003, "直播已结束"),
    LIVE_ROOM_FULL(5004, "直播间人数已满"),
    
    // 文件相关 (6000-6999)
    FILE_UPLOAD_ERROR(6001, "文件上传失败"),
    FILE_TYPE_NOT_SUPPORTED(6002, "文件类型不支持"),
    FILE_SIZE_EXCEEDED(6003, "文件大小超出限制"),
    FILE_NOT_FOUND(6004, "文件不存在"),
    
    // 支付相关 (7000-7999)
    PAYMENT_ERROR(7001, "支付失败"),
    PAYMENT_TIMEOUT(7002, "支付超时"),
    REFUND_ERROR(7003, "退款失败"),
    
    // 系统相关 (8000-8999)
    RATE_LIMIT_EXCEEDED(8001, "请求频率超出限制"),
    SERVICE_BUSY(8002, "服务繁忙，请稍后重试"),
    MAINTENANCE(8003, "系统维护中"),
    
    // 第三方服务相关 (9000-9999)
    SMS_SEND_ERROR(9001, "短信发送失败"),
    EMAIL_SEND_ERROR(9002, "邮件发送失败"),
    OSS_UPLOAD_ERROR(9003, "文件存储失败");
    
    private final Integer code;
    private final String message;
}
