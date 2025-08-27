package com.smartlearning.live.service.impl;

import com.smartlearning.live.entity.LiveRoom;
import com.smartlearning.live.entity.LiveSession;
import com.smartlearning.live.mapper.LiveRoomMapper;
import com.smartlearning.live.mapper.LiveSessionMapper;
import com.smartlearning.live.service.LiveRoomService;
import com.smartlearning.common.exception.BusinessException;
import com.smartlearning.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 直播间服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LiveRoomServiceImpl implements LiveRoomService {

    private final LiveRoomMapper liveRoomMapper;
    private final LiveSessionMapper liveSessionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LiveRoom createRoom(LiveRoom liveRoom) {
        log.info("创建直播间: {}", liveRoom.getTitle());

        // 设置默认值
        liveRoom.setStatus(0); // 未开始
        liveRoom.setCurrentViewers(0);
        liveRoom.setTotalViewers(0);
        liveRoom.setMaxViewers(1000);
        liveRoom.setChatEnabled(liveRoom.getChatEnabled() != null ? liveRoom.getChatEnabled() : true);
        liveRoom.setRecordEnabled(liveRoom.getRecordEnabled() != null ? liveRoom.getRecordEnabled() : false);
        liveRoom.setCreateTime(LocalDateTime.now());
        liveRoom.setUpdateTime(LocalDateTime.now());
        liveRoom.setDeleted(false);

        // 生成流地址
        String streamKey = "live_" + UUID.randomUUID().toString().replace("-", "");
        liveRoom.setStreamUrl("rtmp://localhost:1935/live/" + streamKey);
        liveRoom.setPlayUrl("http://localhost:8080/hls/" + streamKey + ".m3u8");

        // 保存到数据库
        int result = liveRoomMapper.insert(liveRoom);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "创建直播间失败");
        }

        log.info("直播间创建成功: id={}", liveRoom.getId());
        return liveRoom;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startLive(Long roomId) {
        log.info("开始直播: roomId={}", roomId);

        // 检查直播间是否存在
        LiveRoom room = liveRoomMapper.selectById(roomId);
        if (room == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "直播间不存在");
        }

        if (room.getStatus() == 1) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "直播已经开始");
        }

        // 更新直播状态
        int result = liveRoomMapper.startLive(roomId, LocalDateTime.now());
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "开始直播失败");
        }

        // 这里可以添加启动直播流的逻辑
        // 通知相关服务

        log.info("直播已开始: roomId={}", roomId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void stopLive(Long roomId) {
        log.info("结束直播: roomId={}", roomId);

        // 检查直播间是否存在
        LiveRoom room = liveRoomMapper.selectById(roomId);
        if (room == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "直播间不存在");
        }

        if (room.getStatus() != 1) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "直播未开始或已结束");
        }

        // 更新直播状态
        int result = liveRoomMapper.stopLive(roomId, LocalDateTime.now());
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "结束直播失败");
        }

        // 断开所有用户连接
        LocalDateTime expireTime = LocalDateTime.now().minusMinutes(1);
        liveSessionMapper.cleanupExpiredSessions(expireTime);

        // 这里可以添加停止直播流和保存录制文件的逻辑

        log.info("直播已结束: roomId={}", roomId);
    }

    @Override
    public LiveRoom getRoomInfo(Long roomId) {
        log.info("获取直播间信息: roomId={}", roomId);

        LiveRoom room = liveRoomMapper.selectById(roomId);
        if (room == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "直播间不存在");
        }

        // 更新实时观看人数
        Integer currentViewers = liveSessionMapper.selectCurrentViewerCount(roomId);
        room.setCurrentViewers(currentViewers != null ? currentViewers : 0);

        return room;
    }

    @Override
    public List<LiveRoom> getLiveRooms() {
        log.info("获取正在直播的房间列表");

        List<LiveRoom> rooms = liveRoomMapper.selectLiveRooms();

        // 更新每个房间的实时观看人数
        for (LiveRoom room : rooms) {
            Integer currentViewers = liveSessionMapper.selectCurrentViewerCount(room.getId());
            room.setCurrentViewers(currentViewers != null ? currentViewers : 0);
        }

        return rooms;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String joinRoom(Long roomId, Long userId) {
        log.info("用户进入直播间: userId={}, roomId={}", userId, roomId);

        // 验证直播间是否存在
        LiveRoom room = liveRoomMapper.selectById(roomId);
        if (room == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "直播间不存在");
        }

        // 检查直播间是否需要密码
        // if (room.getPassword() != null && !room.getPassword().isEmpty()) {
        // // 验证密码逻辑
        // }

        // 创建会话记录
        LiveSession session = new LiveSession();
        session.setRoomId(roomId);
        session.setUserId(userId);
        session.setRole("student");
        session.setStatus("connected");
        session.setConnectTime(LocalDateTime.now());
        session.setCreateTime(LocalDateTime.now());
        session.setUpdateTime(LocalDateTime.now());

        liveSessionMapper.insert(session);

        // 更新观看人数统计
        Integer currentViewers = liveSessionMapper.selectCurrentViewerCount(roomId);
        liveRoomMapper.updateViewerCount(roomId, currentViewers,
                room.getTotalViewers() + 1,
                Math.max(room.getMaxViewers(), currentViewers));

        return room.getPlayUrl();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void leaveRoom(Long roomId, Long userId) {
        log.info("用户离开直播间: userId={}, roomId={}", userId, roomId);

        // 获取用户最新会话
        LiveSession session = liveSessionMapper.selectLatestUserSession(roomId, userId);
        if (session != null && "connected".equals(session.getStatus())) {
            // 更新会话状态
            liveSessionMapper.updateSessionStatus(session.getSessionId(), "disconnected", LocalDateTime.now());
        }

        // 更新观看人数
        Integer currentViewers = liveSessionMapper.selectCurrentViewerCount(roomId);
        LiveRoom room = liveRoomMapper.selectById(roomId);
        if (room != null) {
            liveRoomMapper.updateViewerCount(roomId, currentViewers,
                    room.getTotalViewers(), room.getMaxViewers());
        }
    }

    @Override
    public Object getRoomStats(Long roomId) {
        log.info("获取直播间统计信息: roomId={}", roomId);

        // 获取基本统计信息
        Map<String, Object> viewStats = liveSessionMapper.selectRoomViewStats(roomId);
        Integer currentViewers = liveSessionMapper.selectCurrentViewerCount(roomId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("roomId", roomId);
        stats.put("currentViewers", currentViewers != null ? currentViewers : 0);
        stats.putAll(viewStats);

        return stats;
    }
}
