package com.smartlearning.user.controller;

import com.smartlearning.common.result.Result;
import com.smartlearning.user.dto.LoginRequest;
import com.smartlearning.user.dto.RegisterRequest;
import com.smartlearning.user.entity.User;
import com.smartlearning.user.service.UserService;
import com.smartlearning.user.vo.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户注册、登录、认证相关接口")
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册接口")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        log.info("用户注册请求: {}", request.getUsername());
        User user = userService.register(request);
        return Result.success("注册成功", user);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        log.info("用户登录请求: {}", request.getUsername());
        String clientIp = getClientIp(httpRequest);
        LoginResponse response = userService.login(request, clientIp);
        return Result.success("登录成功", response);
    }

    /**
     * 刷新Token
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "刷新Token", description = "使用刷新Token获取新的访问Token")
    public Result<LoginResponse> refreshToken(
            @Parameter(description = "刷新Token") @RequestParam String refreshToken) {
        log.info("刷新Token请求");
        LoginResponse response = userService.refreshToken(refreshToken);
        return Result.success("Token刷新成功", response);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的详细信息")
    public Result<User> getUserProfile(@RequestHeader("X-User-Id") Long userId) {
        log.info("获取用户信息请求: userId={}", userId);
        User user = userService.findById(userId);
        if (user != null) {
            // 清除敏感信息
            user.setPassword(null);
        }
        return Result.success("获取成功", user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/profile")
    @Operation(summary = "更新用户信息", description = "更新当前登录用户的信息")
    public Result<User> updateUserProfile(@RequestHeader("X-User-Id") Long userId,
            @RequestBody User user) {
        log.info("更新用户信息请求: userId={}", userId);
        User updatedUser = userService.updateUser(userId, user);
        return Result.success("更新成功", updatedUser);
    }

    /**
     * 检查用户名是否可用
     */
    @GetMapping("/check-username")
    @Operation(summary = "检查用户名", description = "检查用户名是否已被使用")
    public Result<Boolean> checkUsername(@Parameter(description = "用户名") @RequestParam String username) {
        log.info("检查用户名请求: {}", username);
        boolean exists = userService.existsByUsername(username);
        return Result.success("检查完成", !exists);
    }

    /**
     * 检查邮箱是否可用
     */
    @GetMapping("/check-email")
    @Operation(summary = "检查邮箱", description = "检查邮箱是否已被注册")
    public Result<Boolean> checkEmail(@Parameter(description = "邮箱") @RequestParam String email) {
        log.info("检查邮箱请求: {}", email);
        boolean exists = userService.existsByEmail(email);
        return Result.success("检查完成", !exists);
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{userId}")
    @Operation(summary = "根据ID获取用户", description = "根据用户ID获取用户基本信息")
    public Result<User> getUserById(@Parameter(description = "用户ID") @PathVariable Long userId) {
        log.info("根据ID获取用户请求: userId={}", userId);
        User user = userService.findById(userId);
        if (user != null) {
            // 清除敏感信息
            user.setPassword(null);
            user.setPhone(null);
            user.setEmail(null);
        }
        return Result.success("获取成功", user);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户登出接口")
    public Result<Void> logout(@RequestHeader("X-User-Id") Long userId) {
        log.info("用户登出请求: userId={}", userId);
        // 这里可以添加登出逻辑，比如将Token加入黑名单
        // 目前JWT是无状态的，客户端删除Token即可
        return Result.success("登出成功");
    }

    /**
     * 获取用户详细资料
     */
    @GetMapping("/profile/detail")
    @Operation(summary = "获取用户详细资料", description = "获取当前用户的详细资料信息")
    public Result<com.smartlearning.user.entity.UserProfile> getUserProfileDetail(
            @RequestHeader("X-User-Id") Long userId) {
        log.info("获取用户详细资料请求: userId={}", userId);
        com.smartlearning.user.entity.UserProfile profile = userService.getUserProfile(userId);
        return Result.success("获取成功", profile);
    }

    /**
     * 更新用户详细资料
     */
    @PutMapping("/profile/detail")
    @Operation(summary = "更新用户详细资料", description = "更新当前用户的详细资料信息")
    public Result<com.smartlearning.user.entity.UserProfile> updateUserProfile(@RequestHeader("X-User-Id") Long userId,
            @RequestBody com.smartlearning.user.entity.UserProfile profile) {
        log.info("更新用户详细资料请求: userId={}", userId);
        com.smartlearning.user.entity.UserProfile updatedProfile = userService.updateUserProfile(userId, profile);
        return Result.success("更新成功", updatedProfile);
    }

    /**
     * 上传用户头像
     */
    @PostMapping("/avatar")
    @Operation(summary = "上传用户头像", description = "上传并更新用户头像")
    public Result<String> uploadAvatar(@RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "头像文件") @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        log.info("上传用户头像请求: userId={}, filename={}", userId, file.getOriginalFilename());
        String avatarUrl = userService.uploadAvatar(userId, file);
        return Result.success("头像上传成功", avatarUrl);
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/stats")
    @Operation(summary = "获取用户统计信息", description = "获取用户学习统计、积分等信息")
    public Result<com.smartlearning.user.entity.UserStats> getUserStats(@RequestHeader("X-User-Id") Long userId) {
        log.info("获取用户统计信息请求: userId={}", userId);
        com.smartlearning.user.entity.UserStats stats = userService.getUserStats(userId);
        return Result.success("获取成功", stats);
    }

    /**
     * 更新用户状态（管理员接口）
     */
    @PutMapping("/{userId}/status")
    @Operation(summary = "更新用户状态", description = "管理员更新用户状态（启用/禁用）")
    public Result<Boolean> updateUserStatus(@Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "状态") @RequestParam Integer status,
            @RequestHeader("X-User-Id") Long operatorId) {
        log.info("更新用户状态请求: userId={}, status={}, operatorId={}", userId, status, operatorId);
        boolean success = userService.updateUserStatus(userId, status);
        return Result.success("状态更新成功", success);
    }

    /**
     * 批量更新用户状态（管理员接口）
     */
    @PutMapping("/batch/status")
    @Operation(summary = "批量更新用户状态", description = "管理员批量更新用户状态")
    public Result<Boolean> batchUpdateUserStatus(@Parameter(description = "用户ID数组") @RequestParam Long[] userIds,
            @Parameter(description = "状态") @RequestParam Integer status,
            @RequestHeader("X-User-Id") Long operatorId) {
        log.info("批量更新用户状态请求: userIds={}, status={}, operatorId={}", userIds, status, operatorId);
        boolean success = userService.batchUpdateUserStatus(userIds, status);
        return Result.success("批量状态更新成功", success);
    }

    /**
     * 重置用户密码（管理员接口）
     */
    @PutMapping("/{userId}/password/reset")
    @Operation(summary = "重置用户密码", description = "管理员重置用户密码")
    public Result<Boolean> resetPassword(@Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "新密码") @RequestParam String newPassword,
            @RequestHeader("X-User-Id") Long operatorId) {
        log.info("重置用户密码请求: userId={}, operatorId={}", userId, operatorId);
        boolean success = userService.resetPassword(userId, newPassword);
        return Result.success("密码重置成功", success);
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor) && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(xRealIp) && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        String proxyClientIp = request.getHeader("Proxy-Client-IP");
        if (StringUtils.hasText(proxyClientIp) && !"unknown".equalsIgnoreCase(proxyClientIp)) {
            return proxyClientIp;
        }

        String wlProxyClientIp = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtils.hasText(wlProxyClientIp) && !"unknown".equalsIgnoreCase(wlProxyClientIp)) {
            return wlProxyClientIp;
        }

        String httpClientIp = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtils.hasText(httpClientIp) && !"unknown".equalsIgnoreCase(httpClientIp)) {
            return httpClientIp;
        }

        String httpXForwardedFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtils.hasText(httpXForwardedFor) && !"unknown".equalsIgnoreCase(httpXForwardedFor)) {
            return httpXForwardedFor;
        }

        return request.getRemoteAddr();
    }
}
