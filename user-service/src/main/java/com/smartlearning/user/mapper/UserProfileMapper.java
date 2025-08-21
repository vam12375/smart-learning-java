package com.smartlearning.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartlearning.user.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户资料Mapper接口
 */
@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfile> {
}
