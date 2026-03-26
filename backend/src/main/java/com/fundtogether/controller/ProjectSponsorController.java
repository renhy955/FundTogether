package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fundtogether.common.Result;
import com.fundtogether.dto.ProjectCreateDTO;
import com.fundtogether.dto.ProjectUpdateDTO;
import com.fundtogether.entity.Project;
import com.fundtogether.security.LoginUser;
import com.fundtogether.service.ProjectService;
import com.fundtogether.service.SupportOrderService;
import com.fundtogether.vo.SupporterVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sponsor/project")
public class ProjectSponsorController {

    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private SupportOrderService supportOrderService;

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @PostMapping("/create")
    public Result<?> createProject(@RequestBody @Valid ProjectCreateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        
        projectService.createProject(dto, getCurrentUserId());
        return Result.success("项目提交审核成功");
    }

    @PutMapping("/update")
    public Result<?> updateProject(@RequestBody @Valid ProjectUpdateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        
        projectService.updatePendingProject(dto, getCurrentUserId());
        return Result.success("项目修改成功");
    }

    @PutMapping("/update-active")
    public Result<?> updateActiveProject(@RequestBody @Valid ProjectUpdateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        
        projectService.updateActiveProject(dto, getCurrentUserId());
        return Result.success("项目内容修改成功");
    }

    @PutMapping("/cancel/{id}")
    public Result<?> cancelProject(@PathVariable Long id) {
        projectService.cancelPendingProject(id, getCurrentUserId());
        return Result.success("项目取消成功");
    }

    @GetMapping("/my-list")
    public Result<IPage<Project>> getMyProjects(@RequestParam(defaultValue = "1") Integer current,
                                                @RequestParam(defaultValue = "10") Integer size) {
        Page<Project> page = new Page<>(current, size);
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getSponsorId, getCurrentUserId());
        wrapper.orderByDesc(Project::getCreatedAt);
        
        IPage<Project> result = projectService.page(page, wrapper);
        return Result.success(result);
    }

    @GetMapping("/{projectId}/supporters")
    public Result<IPage<SupporterVO>> getProjectSupporters(@PathVariable Long projectId,
                                                           @RequestParam(defaultValue = "1") Integer current,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        IPage<SupporterVO> page = supportOrderService.getProjectSupporters(projectId, getCurrentUserId(), current, size);
        return Result.success(page);
    }
}
