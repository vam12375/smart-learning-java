package com.smartlearning.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.live.entity.LiveRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 直播间Mapper
 */
@Mapper
public interface LiveRoomMapper extends BaseMapper<LiveRoom> {
    
    /**
     * 获取正在直播的房间列表
     */
    @Select("SELECT * FROM live_room WHERE status = 1 AND deleted = 0 ORDER BY actual_start_time DESC")
    List<LiveRoom> selectLiveRooms();
    
    /**
     * 获取用户的直播间列表
     */
    @Select("SELECT * FROM live_room WHERE teacher_id = #{teacherId} AND deleted = 0 ORDER BY create_time DESC")
    List<LiveRoom> selectByTeacherId(@Param("teacherId") Long teacherId);
    
    /**
     * 更新直播间观看人数
     */
    @Update("UPDATE live_room SET current_viewers = #{currentViewers}, " +
            "total_viewers = GREATEST(total_viewers, #{totalViewers}), " +
            "max_viewers = GREATEST(max_viewers, #{maxViewers}) " +
            "WHERE id = #{roomId}")
    int updateViewerCount(@Param("roomId") Long roomId, 
                         @Param("currentViewers") Integer currentViewers,
                         @Param("totalViewers") Integer totalViewers,
                         @Param("maxViewers") Integer maxViewers);
    
    /**
     * 开始直播
     */
    @Update("UPDATE live_room SET status = 1, actual_start_time = #{startTime} WHERE id = #{roomId}")
    int startLive(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime);
    
    /**
     * 结束直播
     */
    @Update("UPDATE live_room SET status = 2, end_time = #{endTime} WHERE id = #{roomId}")
    int stopLive(@Param("roomId") Long roomId, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 更新直播流地址
     */
    @Update("UPDATE live_room SET stream_url = #{streamUrl}, play_url = #{playUrl} WHERE id = #{roomId}")
    int updateStreamUrls(@Param("roomId") Long roomId, 
                        @Param("streamUrl") String streamUrl,
                        @Param("playUrl") String playUrl);
    
    /**
     * 更新录制文件地址
     */
    @Update("UPDATE live_room SET record_url = #{recordUrl} WHERE id = #{roomId}")
    int updateRecordUrl(@Param("roomId") Long roomId, @Param("recordUrl") String recordUrl);
    
    /**
     * 获取课程的直播间
     */
    @Select("SELECT * FROM live_room WHERE course_id = #{courseId} AND deleted = 0 ORDER BY create_time DESC")
    List<LiveRoom> selectByCourseId(@Param("courseId") Long courseId);
    
    /**
     * 统计直播间数量
     */
    @Select("SELECT COUNT(*) FROM live_room WHERE teacher_id = #{teacherId} AND status = #{status} AND deleted = 0")
    Integer countByTeacherAndStatus(@Param("teacherId") Long teacherId, @Param("status") Integer status);
}
