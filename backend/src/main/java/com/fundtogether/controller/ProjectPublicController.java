package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fundtogether.common.Result;
import com.fundtogether.entity.Project;
import com.fundtogether.entity.SupportOrder;
import com.fundtogether.service.ProjectService;
import com.fundtogether.service.SupportOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/project")
public class ProjectPublicController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SupportOrderService supportOrderService;

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
        Project project = projectService.getById(id);
        if (project == null) {
            return Result.error("项目不存在");
        }

        // 查询该项目所有已支付的支持订单
        QueryWrapper<SupportOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("project_id", id)
               .eq("status", 1) // 1-已支付
               .orderByAsc("pay_time");
        List<SupportOrder> orders = supportOrderService.list(wrapper);

        LocalDate startDate = project.getStartTime() != null ? project.getStartTime().toLocalDate() : LocalDate.now().minusDays(14);
        if (!orders.isEmpty() && orders.get(0).getPayTime().toLocalDate().isBefore(startDate)) {
            startDate = orders.get(0).getPayTime().toLocalDate();
        }

        LocalDate today = LocalDate.now();
        if (startDate.isAfter(today)) {
            startDate = today;
        }

        // 按天统计每日新增
        Map<LocalDate, BigDecimal> dailyIncrease = new HashMap<>();
        for (SupportOrder order : orders) {
            if (order.getPayTime() == null) continue;
            LocalDate d = order.getPayTime().toLocalDate();
            dailyIncrease.put(d, dailyIncrease.getOrDefault(d, BigDecimal.ZERO).add(order.getAmount()));
        }

        List<Map<String, Object>> progress = new ArrayList<>();
        BigDecimal currentCumulative = BigDecimal.ZERO;

        // 生成从 startDate 到 today 的每一天的累加数据
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(today)) {
            BigDecimal inc = dailyIncrease.getOrDefault(currentDate, BigDecimal.ZERO);
            currentCumulative = currentCumulative.add(inc);
            
            Map<String, Object> entry = new HashMap<>();
            entry.put("date", currentDate.toString());
            entry.put("amount", currentCumulative);
            progress.add(entry);
            
            currentDate = currentDate.plusDays(1);
        }

        return Result.success(progress);
    }
}
