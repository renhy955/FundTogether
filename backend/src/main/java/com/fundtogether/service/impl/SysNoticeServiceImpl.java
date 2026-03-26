package com.fundtogether.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fundtogether.entity.SysNotice;
import com.fundtogether.mapper.SysNoticeMapper;
import com.fundtogether.service.SysNoticeService;
import org.springframework.stereotype.Service;

@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeService {
}
