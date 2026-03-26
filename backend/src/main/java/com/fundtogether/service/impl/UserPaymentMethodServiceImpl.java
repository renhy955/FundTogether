package com.fundtogether.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fundtogether.entity.UserPaymentMethod;
import com.fundtogether.mapper.UserPaymentMethodMapper;
import com.fundtogether.service.UserPaymentMethodService;
import com.fundtogether.dto.UserPaymentMethodDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserPaymentMethodServiceImpl extends ServiceImpl<UserPaymentMethodMapper, UserPaymentMethod> implements UserPaymentMethodService {
    @Override
    public void bindPaymentMethod(Long userId, UserPaymentMethodDTO dto) {
        UserPaymentMethod method = new UserPaymentMethod();
        BeanUtils.copyProperties(dto, method);
        method.setUserId(userId);
        this.save(method);
    }
    
    @Override
    public List<UserPaymentMethod> getUserPaymentMethods(Long userId) {
        LambdaQueryWrapper<UserPaymentMethod> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPaymentMethod::getUserId, userId);
        return this.list(wrapper);
    }
}
