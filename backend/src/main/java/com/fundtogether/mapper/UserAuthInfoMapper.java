package com.fundtogether.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fundtogether.entity.UserAuthInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthInfoMapper extends BaseMapper<UserAuthInfo> {
}