package com.fundtogether.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fundtogether.entity.UserPaymentMethod;
import com.fundtogether.dto.UserPaymentMethodDTO;
import java.util.List;

public interface UserPaymentMethodService extends IService<UserPaymentMethod> {
    void bindPaymentMethod(Long userId, UserPaymentMethodDTO dto);
    List<UserPaymentMethod> getUserPaymentMethods(Long userId);
}
