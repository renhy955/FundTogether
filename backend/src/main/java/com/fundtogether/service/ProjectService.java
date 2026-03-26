package com.fundtogether.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fundtogether.dto.ProjectCreateDTO;
import com.fundtogether.dto.ProjectUpdateDTO;
import com.fundtogether.entity.Project;

public interface ProjectService extends IService<Project> {
    
    IPage<Project> getOnlineProjects(Integer current, Integer size, Long categoryId, Double minAmount, Double maxAmount, Double minProgress, Double maxProgress, String keyword, Integer sortType);
    
    Project getProjectDetail(Long id);
    
    void createProject(ProjectCreateDTO dto, Long userId);
    
    void updatePendingProject(ProjectUpdateDTO dto, Long userId);
    
    void updateActiveProject(ProjectUpdateDTO dto, Long userId);
    
    void cancelPendingProject(Long projectId, Long userId);
    
    IPage<Project> getPendingProjects(Integer current, Integer size);
    
    void approveProject(Long projectId);
    
    void rejectProject(Long projectId, String reason);
    
    void takedownProject(Long projectId, String reason);
}
