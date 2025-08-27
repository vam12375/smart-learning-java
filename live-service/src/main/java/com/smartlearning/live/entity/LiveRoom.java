package com.smartlearning.live.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 直播间实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("live_room")
public class LiveRoom {
    
    /**
     * 直播间ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 直播间标题
     */
    private String title;
    
    /**
     * 直播间描述
     */
    private String description;
    
    /**
     * 主播用户ID
     */
    private Long teacherId;
    
    /**
     * 主播姓名
     */
    private String teacherName;
    
    /**
     * 课程ID
     */
    private Long courseId;
    
    /**
     * 直播间封面图
     */
    private String coverImage;
    
    /**
     * 直播状态：0-未开始，1-直播中，2-已结束
     */
    private Integer status;
    
    /**
     * 预计开始时间
     */
    private LocalDateTime scheduledStartTime;
    
    /**
     * 实际开始时间
     */
    private LocalDateTime actualStartTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 当前观看人数
     */
    private Integer currentViewers;
    
    /**
     * 累计观看人数
     */
    private Integer totalViewers;
    
    /**
     * 最大观看人数
     */
    private Integer maxViewers;
    
    /**
     * 直播流地址
     */
    private String streamUrl;
    
    /**
     * 播放地址
     */
    private String playUrl;
    
    /**
     * 录制文件地址
     */
    private String recordUrl;
    
    /**
     * 是否允许聊天
     */
    private Boolean chatEnabled;
    
    /**
     * 是否录制
     */
    private Boolean recordEnabled;
    
    /**
     * 直播间密码（可选）
     */
    private String password;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
}
