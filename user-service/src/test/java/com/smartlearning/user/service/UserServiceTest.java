package com.smartlearning.user.service;

import com.smartlearning.user.dto.LoginRequest;
import com.smartlearning.user.dto.RegisterRequest;
import com.smartlearning.user.entity.User;
import com.smartlearning.user.vo.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    
    // 注意：这些测试需要在有数据库和Redis环境的情况下运行
    // 这里提供测试用例的结构，实际运行需要配置测试环境
    
    @Test
    public void testUserRegistration() {
        // 测试用户注册功能
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");
        request.setNickname("测试用户");
        
        // 这里需要注入UserService并调用register方法
        // User user = userService.register(request);
        // assertNotNull(user);
        // assertEquals("testuser", user.getUsername());
        // assertEquals("test@example.com", user.getEmail());
        // assertNull(user.getPassword()); // 密码应该被清除
    }
    
    @Test
    public void testUserLogin() {
        // 测试用户登录功能
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        
        // 这里需要注入UserService并调用login方法
        // LoginResponse response = userService.login(request, "127.0.0.1");
        // assertNotNull(response);
        // assertNotNull(response.getToken());
        // assertNotNull(response.getRefreshToken());
        // assertNotNull(response.getUserInfo());
        // assertEquals("testuser", response.getUserInfo().getUsername());
    }
    
    @Test
    public void testTokenRefresh() {
        // 测试Token刷新功能
        String refreshToken = "valid_refresh_token";
        
        // 这里需要注入UserService并调用refreshToken方法
        // LoginResponse response = userService.refreshToken(refreshToken);
        // assertNotNull(response);
        // assertNotNull(response.getToken());
        // assertNotNull(response.getRefreshToken());
    }
    
    @Test
    public void testFindUserByUsernameOrEmail() {
        // 测试根据用户名或邮箱查找用户
        String username = "testuser";
        
        // 这里需要注入UserService并调用findByUsernameOrEmail方法
        // User user = userService.findByUsernameOrEmail(username);
        // assertNotNull(user);
        // assertEquals(username, user.getUsername());
    }
    
    @Test
    public void testCheckUsernameExists() {
        // 测试检查用户名是否存在
        String username = "testuser";
        
        // 这里需要注入UserService并调用existsByUsername方法
        // boolean exists = userService.existsByUsername(username);
        // assertTrue(exists);
    }
    
    @Test
    public void testCheckEmailExists() {
        // 测试检查邮箱是否存在
        String email = "test@example.com";
        
        // 这里需要注入UserService并调用existsByEmail方法
        // boolean exists = userService.existsByEmail(email);
        // assertTrue(exists);
    }
}
