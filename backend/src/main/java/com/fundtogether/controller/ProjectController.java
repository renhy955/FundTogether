package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fundtogether.common.Result;
import com.fundtogether.entity.Project;
import com.fundtogether.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/recommend")
    public Result<List<Project>> getRecommendProjects(@RequestParam(defaultValue = "5") Integer limit) {
        // Return popular projects based on heat and current amount
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Project::getStatus, 1) // 1-筹款中
               .orderByDesc(Project::getHeat)
               .orderByDesc(Project::getCurrentAmount)
               .last("LIMIT " + limit);
        List<Project> recommendedProjects = projectService.list(wrapper);
        return Result.success(recommendedProjects);
    }
}
