package com.smartlearning.live.dto;

import lombok.Data;

/**
 * WebRTC信令消息DTO
 */
@Data
public class WebRTCSignalMessage {

    /**
     * 消息类型：offer, answer, ice-candidate, join, leave
     */
    private String type;

    /**
     * 发送者用户ID
     */
    private String fromUserId;

    /**
     * 接收者用户ID（可选，广播消息时为空）
     */
    private String toUserId;

    /**
     * 房间ID
     */
    private String roomId;

    /**
     * SDP信息（offer/answer类型消息）
     */
    private String sdp;

    /**
     * ICE候选信息
     */
    private IceCandidate iceCandidate;

    /**
     * 扩展数据
     */
    private Object data;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * ICE候选信息
     */
    @Data
    public static class IceCandidate {
        private String candidate;
        private String sdpMid;
        private Integer sdpMLineIndex;
    }
}
