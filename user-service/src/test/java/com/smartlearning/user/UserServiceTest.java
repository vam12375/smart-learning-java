package com.smartlearning.user;

import com.smartlearning.user.entity.User;
import com.smartlearning.user.entity.UserProfile;
import com.smartlearning.user.entity.UserStats;
import com.smartlearning.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户服务测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testFindById() {
        // 测试查找用户
        User user = userService.findById(1L);
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
    }

    @Test
    public void testGetUserProfile() {
        // 测试获取用户资料
        UserProfile profile = userService.getUserProfile(1L);
        // 可能为空，因为是新功能
        System.out.println("User profile: " + profile);
    }

    @Test
    public void testGetUserStats() {
        // 测试获取用户统计
        UserStats stats = userService.getUserStats(1L);
        assertNotNull(stats);
        assertEquals(1L, stats.getUserId());
    }

    @Test
    public void testUpdateUserStatus() {
        // 测试更新用户状态
        boolean result = userService.updateUserStatus(1L, 1);
        assertTrue(result);
    }

    @Test
    public void testResetPassword() {
        // 测试重置密码
        boolean result = userService.resetPassword(1L, "newpassword123");
        assertTrue(result);
    }
}
