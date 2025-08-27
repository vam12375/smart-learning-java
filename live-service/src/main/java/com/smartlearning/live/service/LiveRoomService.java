package com.smartlearning.live.service;

import com.smartlearning.live.entity.LiveRoom;

import java.util.List;

/**
 * 直播间服务接口
 */
public interface LiveRoomService {
    
    /**
     * 创建直播间
     */
    LiveRoom createRoom(LiveRoom liveRoom);
    
    /**
     * 开始直播
     */
    void startLive(Long roomId);
    
    /**
     * 结束直播
     */
    void stopLive(Long roomId);
    
    /**
     * 获取直播间信息
     */
    LiveRoom getRoomInfo(Long roomId);
    
    /**
     * 获取正在直播的房间列表
     */
    List<LiveRoom> getLiveRooms();
    
    /**
     * 进入直播间
     */
    String joinRoom(Long roomId, Long userId);
    
    /**
     * 离开直播间
     */
    void leaveRoom(Long roomId, Long userId);
    
    /**
     * 获取直播间统计信息
     */
    Object getRoomStats(Long roomId);
}
