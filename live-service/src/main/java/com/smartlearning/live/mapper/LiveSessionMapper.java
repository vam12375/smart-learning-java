package com.smartlearning.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.live.entity.LiveSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 直播会话Mapper
 */
@Mapper
public interface LiveSessionMapper extends BaseMapper<LiveSession> {
    
    /**
     * 获取房间内的活跃会话
     */
    @Select("SELECT * FROM live_sessions WHERE room_id = #{roomId} AND status = 'connected' ORDER BY connect_time")
    List<LiveSession> selectActiveSessionsByRoom(@Param("roomId") Long roomId);
    
    /**
     * 获取用户的会话历史
     */
    @Select("SELECT * FROM live_sessions WHERE user_id = #{userId} ORDER BY create_time DESC LIMIT #{limit}")
    List<LiveSession> selectUserSessions(@Param("userId") Long userId, @Param("limit") Integer limit);
    
    /**
     * 更新会话状态
     */
    @Update("UPDATE live_sessions SET status = #{status}, disconnect_time = #{disconnectTime}, " +
            "duration = TIMESTAMPDIFF(SECOND, connect_time, #{disconnectTime}) " +
            "WHERE session_id = #{sessionId}")
    int updateSessionStatus(@Param("sessionId") String sessionId, 
                           @Param("status") String status,
                           @Param("disconnectTime") LocalDateTime disconnectTime);
    
    /**
     * 统计房间观看数据
     */
    @Select("SELECT " +
            "COUNT(DISTINCT user_id) as unique_viewers, " +
            "COUNT(*) as total_sessions, " +
            "AVG(duration) as avg_duration, " +
            "MAX(duration) as max_duration " +
            "FROM live_sessions WHERE room_id = #{roomId}")
    Map<String, Object> selectRoomViewStats(@Param("roomId") Long roomId);
    
    /**
     * 获取房间的实时观看人数
     */
    @Select("SELECT COUNT(*) FROM live_sessions WHERE room_id = #{roomId} AND status = 'connected'")
    Integer selectCurrentViewerCount(@Param("roomId") Long roomId);
    
    /**
     * 清理过期的连接会话
     */
    @Update("UPDATE live_sessions SET status = 'disconnected', " +
            "disconnect_time = NOW(), " +
            "duration = TIMESTAMPDIFF(SECOND, connect_time, NOW()) " +
            "WHERE status = 'connected' AND connect_time < #{expireTime}")
    int cleanupExpiredSessions(@Param("expireTime") LocalDateTime expireTime);
    
    /**
     * 获取用户在房间的最新会话
     */
    @Select("SELECT * FROM live_sessions WHERE room_id = #{roomId} AND user_id = #{userId} " +
            "ORDER BY create_time DESC LIMIT 1")
    LiveSession selectLatestUserSession(@Param("roomId") Long roomId, @Param("userId") Long userId);
}
