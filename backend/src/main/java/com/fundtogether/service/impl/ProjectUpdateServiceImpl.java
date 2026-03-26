package com.fundtogether.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fundtogether.entity.Project;
import com.fundtogether.entity.ProjectUpdate;
import com.fundtogether.mapper.ProjectMapper;
import com.fundtogether.mapper.ProjectUpdateMapper;
import com.fundtogether.service.ProjectUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectUpdateServiceImpl extends ServiceImpl<ProjectUpdateMapper, ProjectUpdate> implements ProjectUpdateService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public void publishUpdate(Long sponsorId, Long projectId, String title, String content, String images) {
        Project project = projectMapper.selectById(projectId);
        if (project == null || !project.getSponsorId().equals(sponsorId)) {
            throw new RuntimeException("无权操作该项目");
        }
        
        ProjectUpdate update = new ProjectUpdate();
        update.setProjectId(projectId);
        update.setTitle(title);
        update.setContent(content);
        update.setImages(images);
        this.save(update);
        
        // TODO: 给支持者发通知
    }

    @Override
    public List<ProjectUpdate> getProjectUpdates(Long projectId) {
        return this.list(new LambdaQueryWrapper<ProjectUpdate>()
                .eq(ProjectUpdate::getProjectId, projectId)
                .orderByDesc(ProjectUpdate::getCreatedAt));
    }
}
