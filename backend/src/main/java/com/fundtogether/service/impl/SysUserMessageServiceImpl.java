package com.fundtogether.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fundtogether.entity.SysUserMessage;
import com.fundtogether.mapper.SysUserMessageMapper;
import com.fundtogether.service.SysUserMessageService;
import org.springframework.stereotype.Service;

@Service
public class SysUserMessageServiceImpl extends ServiceImpl<SysUserMessageMapper, SysUserMessage> implements SysUserMessageService {

    @Override
    public IPage<SysUserMessage> getMyMessages(Long userId, Integer current, Integer size) {
        Page<SysUserMessage> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUserMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserMessage::getUserId, userId);
        wrapper.orderByDesc(SysUserMessage::getCreatedAt);
        
        return this.page(page, wrapper);
    }

    @Override
    public void markAsRead(Long messageId, Long userId) {
        SysUserMessage message = this.getById(messageId);
        if (message != null && message.getUserId().equals(userId)) {
            message.setIsRead(1);
            this.updateById(message);
        }
    }
}