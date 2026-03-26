package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fundtogether.common.Result;
import com.fundtogether.entity.ProjectReward;
import com.fundtogether.service.ProjectRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/rewards")
public class ProjectRewardController {

    @Autowired
    private ProjectRewardService projectRewardService;

    @PostMapping
    public Result<?> createReward(@PathVariable Long projectId, @RequestBody ProjectReward reward) {
        reward.setProjectId(projectId);
        if (reward.getCurrentCount() == null) {
            reward.setCurrentCount(0);
        }
        projectRewardService.save(reward);
        return Result.success("回报创建成功");
    }

    @GetMapping
    public Result<List<ProjectReward>> getRewardsByProject(@PathVariable Long projectId) {
        LambdaQueryWrapper<ProjectReward> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectReward::getProjectId, projectId)
               .orderByAsc(ProjectReward::getAmount);
        List<ProjectReward> rewards = projectRewardService.list(wrapper);
        return Result.success(rewards);
    }

    @PutMapping("/{id}")
    public Result<?> updateReward(@PathVariable Long projectId, @PathVariable Long id, @RequestBody ProjectReward reward) {
        ProjectReward existing = projectRewardService.getById(id);
        if (existing == null || !existing.getProjectId().equals(projectId)) {
            return Result.error("回报不存在");
        }
        reward.setId(id);
        reward.setProjectId(projectId);
        projectRewardService.updateById(reward);
        return Result.success("回报更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteReward(@PathVariable Long projectId, @PathVariable Long id) {
        ProjectReward existing = projectRewardService.getById(id);
        if (existing == null || !existing.getProjectId().equals(projectId)) {
            return Result.error("回报不存在");
        }
        projectRewardService.removeById(id);
        return Result.success("回报删除成功");
    }
}
