package com.fundtogether.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fundtogether.dto.UserAuthInfoDTO;
import com.fundtogether.entity.UserAuthInfo;

public interface UserAuthInfoService extends IService<UserAuthInfo> {
    void submitAuthInfo(Long userId, UserAuthInfoDTO dto);
    UserAuthInfo getAuthInfo(Long userId);
}