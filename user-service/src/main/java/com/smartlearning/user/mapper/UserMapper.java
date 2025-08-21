package com.smartlearning.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名或邮箱查询用户
     */
    User findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
    
    /**
     * 更新最后登录信息
     */
    int updateLastLoginInfo(@Param("userId") Long userId, @Param("loginTime") java.time.LocalDateTime loginTime, @Param("loginIp") String loginIp);
}
