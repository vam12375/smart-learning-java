package com.smartlearning.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.user.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户资料Mapper接口
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {

    /**
     * 根据用户ID查询用户资料
     */
    UserProfile selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID删除用户资料
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 更新用户资料
     */
    int updateByUserId(@Param("userId") Long userId, @Param("profile") UserProfile profile);
}
