package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fundtogether.common.Result;
import com.fundtogether.entity.FundingLedger;
import com.fundtogether.entity.Project;
import com.fundtogether.entity.ProjectPayout;
import com.fundtogether.entity.SysUser;
import com.fundtogether.security.LoginUser;
import com.fundtogether.service.FundingLedgerService;
import com.fundtogether.service.ProjectPayoutService;
import com.fundtogether.service.ProjectService;
import com.fundtogether.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/funding")
public class FundingController {

    @Autowired
    private ProjectPayoutService projectPayoutService;
    
    @Autowired
    private FundingLedgerService fundingLedgerService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SysUserService sysUserService;

    private LoginUser getCurrentUser() {
        return (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // List payouts for a project
    @GetMapping("/payouts")
    public Result<IPage<ProjectPayout>> getPayouts(@RequestParam(required = false) Long projectId,
                                                   @RequestParam(defaultValue = "1") Integer current,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        LoginUser user = getCurrentUser();
        LambdaQueryWrapper<ProjectPayout> wrapper = new LambdaQueryWrapper<>();
        if (projectId != null) {
            wrapper.eq(ProjectPayout::getProjectId, projectId);
        }
        // If not admin, can only see their own projects
        if (user.getRole() != 3) {
            wrapper.eq(ProjectPayout::getSponsorId, user.getId());
        }
        wrapper.orderByAsc(ProjectPayout::getStage);
        
        Page<ProjectPayout> page = new Page<>(current, size);
        IPage<ProjectPayout> result = projectPayoutService.page(page, wrapper);
        for (ProjectPayout payout : result.getRecords()) {
            Project project = projectService.getById(payout.getProjectId());
            if (project != null) {
                payout.setProjectName(project.getTitle());
            }
            SysUser sponsor = sysUserService.getById(payout.getSponsorId());
            if (sponsor != null) {
                payout.setSponsorName(sponsor.getNickname());
            }
        }
        return Result.success(result);
    }

    // Process a payout (Admin only)
    @PostMapping("/process-payout/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> processPayout(@PathVariable Long id) {
        ProjectPayout payout = projectPayoutService.getById(id);
        if (payout == null) {
            return Result.error("拨付记录不存在");
        }
        if (payout.getStatus() == 1) {
            return Result.error("该记录已拨付");
        }
        
        // Update payout status
        payout.setStatus(1); // 已拨付
        payout.setPayoutTime(LocalDateTime.now());
        projectPayoutService.updateById(payout);
        
        // Record in ledger
        FundingLedger ledger = new FundingLedger();
        ledger.setProjectId(payout.getProjectId());
        ledger.setUserId(payout.getSponsorId());
        ledger.setAmount(payout.getAmount());
        ledger.setType(3); // 3-阶段拨付给发起人
        ledger.setStatus(1); // 成功
        ledger.setRemark(payout.getConditionDesc());
        fundingLedgerService.save(ledger);
        
        return Result.success("拨付成功");
    }

    // List transaction ledgers
    @GetMapping("/ledgers")
    public Result<IPage<FundingLedger>> getLedgers(@RequestParam(required = false) Long projectId,
                                                   @RequestParam(defaultValue = "1") Integer current,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        LoginUser user = getCurrentUser();
        LambdaQueryWrapper<FundingLedger> wrapper = new LambdaQueryWrapper<>();
        if (projectId != null) {
            wrapper.eq(FundingLedger::getProjectId, projectId);
        }
        // If not admin, can only see their own transactions (either as supporter or sponsor)
        if (user.getRole() != 3) {
            wrapper.eq(FundingLedger::getUserId, user.getId());
        }
        wrapper.orderByDesc(FundingLedger::getCreatedAt);
        
        Page<FundingLedger> page = new Page<>(current, size);
        IPage<FundingLedger> result = fundingLedgerService.page(page, wrapper);
        for (FundingLedger ledger : result.getRecords()) {
            Project project = projectService.getById(ledger.getProjectId());
            if (project != null) {
                ledger.setProjectName(project.getTitle());
            }
            SysUser u = sysUserService.getById(ledger.getUserId());
            if (u != null) {
                ledger.setUserName(u.getNickname());
            }
        }
        return Result.success(result);
    }
}
