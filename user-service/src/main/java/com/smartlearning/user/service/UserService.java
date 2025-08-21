package com.smartlearning.user.service;

import com.smartlearning.user.dto.LoginRequest;
import com.smartlearning.user.dto.RegisterRequest;
import com.smartlearning.user.entity.User;
import com.smartlearning.user.entity.UserProfile;
import com.smartlearning.user.entity.UserStats;
import com.smartlearning.user.vo.LoginResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    User register(RegisterRequest request);

    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request, String clientIp);

    /**
     * 刷新Token
     */
    LoginResponse refreshToken(String refreshToken);

    /**
     * 根据用户名或邮箱查询用户
     */
    User findByUsernameOrEmail(String usernameOrEmail);

    /**
     * 根据ID查询用户
     */
    User findById(Long userId);

    /**
     * 更新用户信息
     */
    User updateUser(Long userId, User user);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 获取用户资料
     */
    UserProfile getUserProfile(Long userId);

    /**
     * 更新用户资料
     */
    UserProfile updateUserProfile(Long userId, UserProfile profile);

    /**
     * 上传用户头像
     */
    String uploadAvatar(Long userId, MultipartFile file);

    /**
     * 获取用户统计信息
     */
    UserStats getUserStats(Long userId);

    /**
     * 更新用户状态
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 批量更新用户状态
     */
    boolean batchUpdateUserStatus(Long[] userIds, Integer status);

    /**
     * 重置用户密码
     */
    boolean resetPassword(Long userId, String newPassword);
}
