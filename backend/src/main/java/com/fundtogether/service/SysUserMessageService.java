package com.fundtogether.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fundtogether.entity.SysUserMessage;

public interface SysUserMessageService extends IService<SysUserMessage> {
    IPage<SysUserMessage> getMyMessages(Long userId, Integer current, Integer size);
    void markAsRead(Long messageId, Long userId);
}