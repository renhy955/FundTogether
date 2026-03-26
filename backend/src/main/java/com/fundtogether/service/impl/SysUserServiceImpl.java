package com.fundtogether.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fundtogether.dto.*;
import com.fundtogether.entity.SysUser;
import com.fundtogether.mapper.SysUserMapper;
import com.fundtogether.service.SysUserService;
import com.fundtogether.utils.JwtUtils;
import com.fundtogether.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO registerDTO) {
        // 1. 校验密码一致性
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new RuntimeException("两次密码输入不一致");
        }
        
        // 2. 校验验证码（这里简单模拟，假设验证码固定为 123456）
        if (!"123456".equals(registerDTO.getCode())) {
            throw new RuntimeException("验证码错误");
        }
        
        // 3. 校验账号是否已被注册
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount, registerDTO.getAccount());
        long count = this.count(wrapper);
        if (count > 0) {
            throw new RuntimeException("账号已被注册");
        }
        
        // 4. 生成用户
        SysUser user = new SysUser();
        user.setAccount(registerDTO.getAccount());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(1); // 默认 1-普通支持者
        user.setStatus(1); // 默认 1-正常
        
        if ("phone".equals(registerDTO.getRegisterType())) {
            user.setPhone(registerDTO.getAccount());
        } else if ("email".equals(registerDTO.getRegisterType())) {
            user.setEmail(registerDTO.getAccount());
        }
        
        this.save(user);
    }

    @Override
    public LoginVO login(UserLoginDTO loginDTO) {
        // 1. 根据账号查询用户 (支持账号/手机号/邮箱登录)
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount, loginDTO.getAccount())
               .or().eq(SysUser::getPhone, loginDTO.getAccount())
               .or().eq(SysUser::getEmail, loginDTO.getAccount());
        
        SysUser user = this.getOne(wrapper);
        if (user == null) {
            throw new RuntimeException("账号不存在或密码错误");
        }
        
        // 2. 校验密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("账号不存在或密码错误");
        }
        
        // 3. 校验账号状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        
        // 4. 生成Token
        String token = JwtUtils.generateToken(user.getId(), user.getAccount(), user.getRole());
        
        // 5. 封装返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setId(user.getId());
        loginVO.setAccount(user.getAccount());
        loginVO.setNickname(user.getNickname());
        loginVO.setAvatar(user.getAvatar());
        loginVO.setRole(user.getRole());
        
        return loginVO;
    }

    @Override
    public void updatePassword(Long userId, UserUpdatePasswordDTO dto) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.updateById(user);
    }

    @Override
    public void resetPassword(UserResetPasswordDTO dto) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount, dto.getAccount());
        SysUser user = this.getOne(wrapper);
        
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }
        
        // Validate code (Mock validation for now, assume any code length > 0 is valid)
        if (dto.getCode() == null || dto.getCode().isEmpty()) {
            throw new RuntimeException("验证码无效");
        }
        
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.updateById(user);
    }

    @Override
    public void updateProfile(Long userId, UserProfileUpdateDTO dto) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getAvatar() != null) user.setAvatar(dto.getAvatar());
        if (dto.getBio() != null) user.setBio(dto.getBio());
        
        this.updateById(user);
    }
}
