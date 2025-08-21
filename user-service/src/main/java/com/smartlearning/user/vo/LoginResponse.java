package com.smartlearning.user.vo;

import lombok.Data;

/**
 * 登录响应VO
 */
@Data
public class LoginResponse {
    
    /**
     * 访问Token
     */
    private String token;
    
    /**
     * 刷新Token
     */
    private String refreshToken;
    
    /**
     * Token过期时间(秒)
     */
    private Long expiresIn;
    
    /**
     * 用户信息
     */
    private UserInfo userInfo;
    
    @Data
    public static class UserInfo {
        /**
         * 用户ID
         */
        private Long id;
        
        /**
         * 用户名
         */
        private String username;
        
        /**
         * 邮箱
         */
        private String email;
        
        /**
         * 昵称
         */
        private String nickname;
        
        /**
         * 头像
         */
        private String avatar;
        
        /**
         * 角色
         */
        private String role;
    }
}
