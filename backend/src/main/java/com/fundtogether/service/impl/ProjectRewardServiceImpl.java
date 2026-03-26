package com.fundtogether.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fundtogether.entity.ProjectReward;
import com.fundtogether.mapper.ProjectRewardMapper;
import com.fundtogether.service.ProjectRewardService;
import org.springframework.stereotype.Service;

@Service
public class ProjectRewardServiceImpl extends ServiceImpl<ProjectRewardMapper, ProjectReward> implements ProjectRewardService {
}
