package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fundtogether.common.Result;
import com.fundtogether.dto.UserRoleUpdateDTO;
import com.fundtogether.dto.CategoryStatExcelDTO;
import com.fundtogether.entity.SysUser;
import com.fundtogether.entity.Project;
import com.fundtogether.service.SysUserService;
import com.fundtogether.service.ProjectService;
import com.fundtogether.mapper.ProjectMapper;
import com.fundtogether.mapper.SupportOrderMapper;
import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private SupportOrderMapper supportOrderMapper;

    @GetMapping("/users")
    public Result<IPage<SysUser>> getUsers(@RequestParam(defaultValue = "1") Integer current,
                                           @RequestParam(defaultValue = "10") Integer size) {
        Page<SysUser> page = new Page<>(current, size);
        IPage<SysUser> userPage = sysUserService.page(page);
        // Desensitize password
        userPage.getRecords().forEach(u -> u.setPassword(null));
        return Result.success(userPage);
    }

    @PostMapping("/user/role")
    public Result<?> updateUserRole(@RequestBody @Valid UserRoleUpdateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        
        SysUser user = sysUserService.getById(dto.getUserId());
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        user.setRole(dto.getRole());
        sysUserService.updateById(user);
        
        return Result.success("角色修改成功");
    }

    @GetMapping("/projects/pending")
    public Result<IPage<Project>> getPendingProjects(@RequestParam(defaultValue = "1") Integer current,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        IPage<Project> page = projectService.getPendingProjects(current, size);
        return Result.success(page);
    }

    @GetMapping("/projects/all")
    public Result<IPage<Project>> getAllProjects(@RequestParam(defaultValue = "1") Integer current,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Project> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Project> wrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        wrapper.orderByDesc(Project::getCreatedAt);
        IPage<Project> projectPage = projectService.page(page, wrapper);
        return Result.success(projectPage);
    }

    @PutMapping("/projects/approve/{id}")
    public Result<?> approveProject(@PathVariable Long id) {
        projectService.approveProject(id);
        return Result.success("项目审核通过");
    }

    @PutMapping("/projects/reject/{id}")
    public Result<?> rejectProject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        if (reason == null || reason.trim().isEmpty()) {
            return Result.error("驳回原因不能为空");
        }
        projectService.rejectProject(id, reason);
        return Result.success("项目已驳回");
    }

    @PutMapping("/projects/takedown/{id}")
    public Result<?> takedownProject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        if (reason == null || reason.trim().isEmpty()) {
            return Result.error("下架原因不能为空");
        }
        projectService.takedownProject(id, reason);
        return Result.success("项目已下架");
    }

    @PutMapping("/user/status/{id}")
    public Result<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Integer status = (Integer) body.get("status");
        String disableReason = (String) body.get("disableReason");
        if (status == null) {
            return Result.error("状态不能为空");
        }
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setStatus(status);
        if (status == 0) {
            user.setDisableReason(disableReason);
        } else {
            user.setDisableReason(null);
        }
        sysUserService.updateById(user);
        return Result.success("用户状态更新成功");
    }

    @GetMapping("/stats/platform")
    public Result<Map<String, Object>> getPlatformStats() {
        Map<String, Object> stats = new HashMap<>();
        BigDecimal totalAmount = supportOrderMapper.getTotalAmount();
        Long projectCount = projectService.count();
        Long activeUsers = sysUserService.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getStatus, 1));
        
        stats.put("totalAmount", totalAmount);
        stats.put("projectCount", projectCount);
        stats.put("activeUsers", activeUsers);
        
        return Result.success(stats);
    }

    @GetMapping("/stats/category")
    public Result<List<Map<String, Object>>> getCategoryStats() {
        List<Map<String, Object>> stats = projectMapper.getCategoryStats();
        return Result.success(stats);
    }

    @GetMapping("/stats/export")
    public void exportStats(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("分类统计数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        List<Map<String, Object>> stats = projectMapper.getCategoryStats();
        List<CategoryStatExcelDTO> dtoList = new ArrayList<>();
        
        for (Map<String, Object> stat : stats) {
            CategoryStatExcelDTO dto = new CategoryStatExcelDTO();
            dto.setCategoryName((String) stat.get("categoryName"));
            dto.setProjectCount(((Number) stat.get("projectCount")).longValue());
            Object totalAmountObj = stat.get("totalAmount");
            if (totalAmountObj instanceof BigDecimal) {
                dto.setTotalAmount((BigDecimal) totalAmountObj);
            } else if (totalAmountObj instanceof Number) {
                dto.setTotalAmount(new BigDecimal(totalAmountObj.toString()));
            } else {
                dto.setTotalAmount(BigDecimal.ZERO);
            }
            dtoList.add(dto);
        }

        EasyExcel.write(response.getOutputStream(), CategoryStatExcelDTO.class)
                 .sheet("分类统计")
                 .doWrite(dtoList);
    }
}
