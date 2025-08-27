package com.smartlearning.live.controller;

import com.smartlearning.common.result.Result;
import com.smartlearning.live.dto.LiveRoomCreateRequest;
import com.smartlearning.live.entity.LiveRoom;
import com.smartlearning.live.service.LiveRoomService;
import com.smartlearning.live.service.WebRTCSignalingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 直播间控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/live/room")
@RequiredArgsConstructor
@Tag(name = "直播间管理", description = "直播间相关接口")
public class LiveRoomController {

    private final LiveRoomService liveRoomService;
    private final WebRTCSignalingService signalingService;

    /**
     * 创建直播间
     */
    @PostMapping("/create")
    @Operation(summary = "创建直播间")
    public Result<LiveRoom> createRoom(@RequestBody LiveRoom liveRoom) {
        log.info("创建直播间: {}", liveRoom.getTitle());
        LiveRoom created = liveRoomService.createRoom(liveRoom);
        return Result.success(created);
    }

    /**
     * 开始直播
     */
    @PostMapping("/{roomId}/start")
    @Operation(summary = "开始直播")
    public Result<Void> startLive(@PathVariable Long roomId) {
        log.info("开始直播: roomId={}", roomId);
        liveRoomService.startLive(roomId);
        return Result.success();
    }

    /**
     * 结束直播
     */
    @PostMapping("/{roomId}/stop")
    @Operation(summary = "结束直播")
    public Result<Void> stopLive(@PathVariable Long roomId) {
        log.info("结束直播: roomId={}", roomId);
        liveRoomService.stopLive(roomId);
        return Result.success();
    }

    /**
     * 获取直播间信息
     */
    @GetMapping("/{roomId}")
    @Operation(summary = "获取直播间信息")
    public Result<LiveRoom> getRoomInfo(@PathVariable Long roomId) {
        LiveRoom room = liveRoomService.getRoomInfo(roomId);
        return Result.success(room);
    }

    /**
     * 获取正在直播的房间列表
     */
    @GetMapping("/live")
    @Operation(summary = "获取正在直播的房间列表")
    public Result<List<LiveRoom>> getLiveRooms() {
        List<LiveRoom> rooms = liveRoomService.getLiveRooms();
        return Result.success(rooms);
    }

    /**
     * 进入直播间
     */
    @PostMapping("/{roomId}/join")
    @Operation(summary = "进入直播间")
    public Result<String> joinRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        log.info("用户进入直播间: userId={}, roomId={}", userId, roomId);
        String playUrl = liveRoomService.joinRoom(roomId, userId);
        return Result.success(playUrl);
    }

    /**
     * 离开直播间
     */
    @PostMapping("/{roomId}/leave")
    @Operation(summary = "离开直播间")
    public Result<Void> leaveRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        log.info("用户离开直播间: userId={}, roomId={}", userId, roomId);
        liveRoomService.leaveRoom(roomId, userId);
        return Result.success();
    }

    /**
     * 获取直播间统计信息
     */
    @GetMapping("/{roomId}/stats")
    @Operation(summary = "获取直播间统计信息")
    public Result<Object> getRoomStats(@PathVariable Long roomId) {
        Object stats = liveRoomService.getRoomStats(roomId);
        return Result.success(stats);
    }

    /**
     * 获取房间内用户列表
     */
    @GetMapping("/{roomId}/users")
    @Operation(summary = "获取房间内用户列表", description = "获取指定直播间内的用户列表")
    public Result<List<Map<String, Object>>> getRoomUsers(
            @Parameter(description = "直播间ID") @PathVariable String roomId) {
        log.info("获取房间用户列表: roomId={}", roomId);
        List<Map<String, Object>> users = signalingService.getRoomUsers(roomId);
        return Result.success("获取成功", users);
    }

    /**
     * 获取房间WebRTC统计信息
     */
    @GetMapping("/{roomId}/webrtc-stats")
    @Operation(summary = "获取房间WebRTC统计信息", description = "获取指定直播间的WebRTC连接统计信息")
    public Result<Map<String, Object>> getWebRTCStats(
            @Parameter(description = "直播间ID") @PathVariable String roomId) {
        log.info("获取WebRTC统计信息: roomId={}", roomId);
        Map<String, Object> stats = signalingService.getRoomStats(roomId);
        return Result.success("获取成功", stats);
    }
}
