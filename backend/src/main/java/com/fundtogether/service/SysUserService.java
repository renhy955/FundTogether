package com.fundtogether.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fundtogether.dto.*;
import com.fundtogether.entity.SysUser;
import com.fundtogether.vo.LoginVO;

public interface SysUserService extends IService<SysUser> {
    
    void register(UserRegisterDTO registerDTO);
    
    LoginVO login(UserLoginDTO loginDTO);
    
    void updatePassword(Long userId, UserUpdatePasswordDTO dto);
    
    void resetPassword(UserResetPasswordDTO dto);
    
    void updateProfile(Long userId, UserProfileUpdateDTO dto);
}
