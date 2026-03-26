package com.fundtogether.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fundtogether.entity.ProjectUpdate;
import java.util.List;

public interface ProjectUpdateService extends IService<ProjectUpdate> {
    void publishUpdate(Long sponsorId, Long projectId, String title, String content, String images);
    List<ProjectUpdate> getProjectUpdates(Long projectId);
}
