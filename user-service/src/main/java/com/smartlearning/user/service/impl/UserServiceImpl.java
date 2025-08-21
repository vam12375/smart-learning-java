package com.smartlearning.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartlearning.common.constant.CommonConstants;
import com.smartlearning.common.exception.BusinessException;
import com.smartlearning.common.result.ResultCode;
import com.smartlearning.common.util.JwtUtil;
import com.smartlearning.common.util.PasswordUtil;
import com.smartlearning.user.dto.LoginRequest;
import com.smartlearning.user.dto.RegisterRequest;
import com.smartlearning.user.entity.User;
import com.smartlearning.user.mapper.UserMapper;
import com.smartlearning.user.service.UserService;
import com.smartlearning.user.vo.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterRequest request) {
        log.info("用户注册请求: {}", request.getUsername());
        
        // 验证密码确认
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(ResultCode.VALIDATION_ERROR, "两次输入的密码不一致");
        }
        
        // 检查用户名是否已存在
        if (existsByUsername(request.getUsername())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (existsByEmail(request.getEmail())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "邮箱已被注册");
        }
        
        // 创建用户对象
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(PasswordUtil.encode(request.getPassword()));
        user.setNickname(StringUtils.hasText(request.getNickname()) ? 
                        request.getNickname() : request.getUsername());
        user.setRole(StringUtils.hasText(request.getRole()) ? 
                    request.getRole() : CommonConstants.UserRole.STUDENT);
        user.setStatus(CommonConstants.UserStatus.ENABLED);
        user.setAvatarUrl(CommonConstants.DEFAULT_AVATAR);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        // 保存用户
        int result = userMapper.insert(user);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "用户注册失败");
        }
        
        log.info("用户注册成功: userId={}, username={}", user.getId(), user.getUsername());
        
        // 清除密码字段
        user.setPassword(null);
        return user;
    }
    
    @Override
    public LoginResponse login(LoginRequest request, String clientIp) {
        log.info("用户登录请求: {}", request.getUsername());
        
        // 查询用户
        User user = findByUsernameOrEmail(request.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        
        // 检查用户状态
        if (user.getStatus() == null || user.getStatus() == CommonConstants.UserStatus.DISABLED) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        
        // 验证密码
        if (!PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        
        // 生成Token
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername(), user.getRole());
        
        // 更新最后登录信息
        updateLastLoginInfo(user.getId(), clientIp);
        
        // 缓存用户信息
        cacheUserInfo(user);
        
        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(86400L); // 24小时
        
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatarUrl());
        userInfo.setRole(user.getRole());
        response.setUserInfo(userInfo);
        
        log.info("用户登录成功: userId={}, username={}", user.getId(), user.getUsername());
        return response;
    }
    
    @Override
    public LoginResponse refreshToken(String refreshToken) {
        log.info("刷新Token请求");
        
        // 验证刷新Token
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID, "刷新Token无效或已过期");
        }
        
        // 从Token中获取用户信息
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        String role = jwtUtil.getRoleFromToken(refreshToken);
        
        if (userId == null || !StringUtils.hasText(username)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID, "Token中用户信息无效");
        }
        
        // 验证用户是否存在且状态正常
        User user = findById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        if (user.getStatus() == null || user.getStatus() == CommonConstants.UserStatus.DISABLED) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        
        // 生成新的Token
        String newAccessToken = jwtUtil.generateAccessToken(userId, username, role);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, username, role);
        
        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(86400L); // 24小时
        
        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatarUrl());
        userInfo.setRole(user.getRole());
        response.setUserInfo(userInfo);
        
        log.info("Token刷新成功: userId={}, username={}", userId, username);
        return response;
    }
    
    @Override
    public User findByUsernameOrEmail(String usernameOrEmail) {
        if (!StringUtils.hasText(usernameOrEmail)) {
            return null;
        }
        
        // 先从缓存查询
        String cacheKey = CommonConstants.CachePrefix.USER_INFO + usernameOrEmail;
        User cachedUser = (User) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }
        
        // 从数据库查询
        User user = userMapper.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        
        // 缓存查询结果
        if (user != null) {
            cacheUserInfo(user);
        }
        
        return user;
    }
    
    @Override
    public User findById(Long userId) {
        if (userId == null) {
            return null;
        }
        
        // 先从缓存查询
        String cacheKey = CommonConstants.CachePrefix.USER_INFO + userId;
        User cachedUser = (User) redisTemplate.opsForValue().get(cacheKey);
        if (cachedUser != null) {
            return cachedUser;
        }
        
        // 从数据库查询
        User user = userMapper.selectById(userId);
        
        // 缓存查询结果
        if (user != null) {
            cacheUserInfo(user);
        }
        
        return user;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(Long userId, User user) {
        if (userId == null || user == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "参数不能为空");
        }
        
        // 检查用户是否存在
        User existingUser = findById(userId);
        if (existingUser == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        
        // 更新用户信息
        user.setId(userId);
        user.setUpdateTime(LocalDateTime.now());
        
        int result = userMapper.updateById(user);
        if (result <= 0) {
            throw new BusinessException(ResultCode.ERROR, "用户信息更新失败");
        }
        
        // 清除缓存
        clearUserCache(userId, existingUser.getUsername(), existingUser.getEmail());
        
        // 返回更新后的用户信息
        return findById(userId);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userMapper.selectCount(queryWrapper) > 0;
    }
    
    @Override
    public boolean existsByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return userMapper.selectCount(queryWrapper) > 0;
    }
    
    /**
     * 更新最后登录信息
     */
    private void updateLastLoginInfo(Long userId, String clientIp) {
        try {
            userMapper.updateLastLoginInfo(userId, LocalDateTime.now(), clientIp);
        } catch (Exception e) {
            log.warn("更新最后登录信息失败: userId={}, error={}", userId, e.getMessage());
        }
    }
    
    /**
     * 缓存用户信息
     */
    private void cacheUserInfo(User user) {
        if (user == null) {
            return;
        }
        
        try {
            // 清除密码字段
            User cacheUser = new User();
            cacheUser.setId(user.getId());
            cacheUser.setUsername(user.getUsername());
            cacheUser.setEmail(user.getEmail());
            cacheUser.setPhone(user.getPhone());
            cacheUser.setNickname(user.getNickname());
            cacheUser.setAvatarUrl(user.getAvatarUrl());
            cacheUser.setGender(user.getGender());
            cacheUser.setBirthday(user.getBirthday());
            cacheUser.setRole(user.getRole());
            cacheUser.setStatus(user.getStatus());
            cacheUser.setLastLoginTime(user.getLastLoginTime());
            cacheUser.setLastLoginIp(user.getLastLoginIp());
            cacheUser.setCreateTime(user.getCreateTime());
            cacheUser.setUpdateTime(user.getUpdateTime());
            
            // 多个缓存键
            String userIdKey = CommonConstants.CachePrefix.USER_INFO + user.getId();
            String usernameKey = CommonConstants.CachePrefix.USER_INFO + user.getUsername();
            String emailKey = CommonConstants.CachePrefix.USER_INFO + user.getEmail();
            
            redisTemplate.opsForValue().set(userIdKey, cacheUser, 
                    CommonConstants.CacheExpire.USER_INFO, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(usernameKey, cacheUser, 
                    CommonConstants.CacheExpire.USER_INFO, TimeUnit.SECONDS);
            if (StringUtils.hasText(user.getEmail())) {
                redisTemplate.opsForValue().set(emailKey, cacheUser, 
                        CommonConstants.CacheExpire.USER_INFO, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.warn("缓存用户信息失败: userId={}, error={}", user.getId(), e.getMessage());
        }
    }
    
    /**
     * 清除用户缓存
     */
    private void clearUserCache(Long userId, String username, String email) {
        try {
            String userIdKey = CommonConstants.CachePrefix.USER_INFO + userId;
            String usernameKey = CommonConstants.CachePrefix.USER_INFO + username;
            String emailKey = CommonConstants.CachePrefix.USER_INFO + email;
            
            redisTemplate.delete(userIdKey);
            redisTemplate.delete(usernameKey);
            if (StringUtils.hasText(email)) {
                redisTemplate.delete(emailKey);
            }
        } catch (Exception e) {
            log.warn("清除用户缓存失败: userId={}, error={}", userId, e.getMessage());
        }
    }
}
