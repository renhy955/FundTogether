package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fundtogether.common.Result;
import com.fundtogether.entity.Project;
import com.fundtogether.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/project")
public class ProjectPublicController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/list")
    public Result<IPage<Project>> getProjectList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) Double minProgress,
            @RequestParam(required = false) Double maxProgress,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer sortType) {
        IPage<Project> page = projectService.getOnlineProjects(current, size, categoryId, minAmount, maxAmount, minProgress, maxProgress, keyword, sortType);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<Project> getProjectDetail(@PathVariable Long id) {
        Project project = projectService.getProjectDetail(id);
        return Result.success(project);
    }

    @GetMapping("/{id}/funding-progress")
    public Result<?> getProjectFundingProgress(@PathVariable Long id) {
        // Mock daily funding progress data for demonstration
        // In a real app, this would be queried from the database (e.g., daily aggregation of support orders)
        java.util.List<java.util.Map<String, Object>> progress = new java.util.ArrayList<>();
        java.time.LocalDate today = java.time.LocalDate.now();
        java.util.Random random = new java.util.Random();
        double currentAmount = 0;
        for (int i = 14; i >= 0; i--) {
            java.time.LocalDate date = today.minusDays(i);
            double dailyAmount = random.nextInt(5000) + 500;
            currentAmount += dailyAmount;
            java.util.Map<String, Object> entry = new java.util.HashMap<>();
            entry.put("date", date.toString());
            entry.put("amount", currentAmount);
            progress.add(entry);
        }
        return Result.success(progress);
    }
}
