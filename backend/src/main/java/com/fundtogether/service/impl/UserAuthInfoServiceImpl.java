package com.fundtogether.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fundtogether.dto.UserAuthInfoDTO;
import com.fundtogether.entity.UserAuthInfo;
import com.fundtogether.mapper.UserAuthInfoMapper;
import com.fundtogether.service.UserAuthInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements UserAuthInfoService {

    @Override
    public void submitAuthInfo(Long userId, UserAuthInfoDTO dto) {
        LambdaQueryWrapper<UserAuthInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAuthInfo::getUserId, userId);
        UserAuthInfo existing = this.getOne(wrapper);
        
        if (existing != null) {
            if (existing.getStatus() == 0 || existing.getStatus() == 1) {
                throw new RuntimeException("实名认证已在审核中或已通过，不可重复提交");
            }
            BeanUtils.copyProperties(dto, existing);
            existing.setStatus(0);
            this.updateById(existing);
        } else {
            UserAuthInfo authInfo = new UserAuthInfo();
            BeanUtils.copyProperties(dto, authInfo);
            authInfo.setUserId(userId);
            authInfo.setStatus(0);
            this.save(authInfo);
        }
    }

    @Override
    public UserAuthInfo getAuthInfo(Long userId) {
        LambdaQueryWrapper<UserAuthInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAuthInfo::getUserId, userId);
        return this.getOne(wrapper);
    }
}