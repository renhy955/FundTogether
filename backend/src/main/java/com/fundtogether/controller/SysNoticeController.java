package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fundtogether.common.Result;
import com.fundtogether.entity.SysNotice;
import com.fundtogether.entity.SysUser;
import com.fundtogether.entity.SysUserMessage;
import com.fundtogether.service.SysNoticeService;
import com.fundtogether.service.SysUserMessageService;
import com.fundtogether.service.SysUserService;
import com.fundtogether.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notices")
public class SysNoticeController {

    @Autowired
    private SysNoticeService sysNoticeService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMessageService sysUserMessageService;

    private void handleNoticePublished(SysNotice notice) {
        if (notice.getStatus() != null && notice.getStatus() == 1) {
            List<SysUser> users = sysUserService.list();
            List<SysUserMessage> messages = users.stream().map(user -> {
                SysUserMessage msg = new SysUserMessage();
                msg.setUserId(user.getId());
                msg.setType(1); // 1-系统公告
                msg.setTitle("新系统公告: " + notice.getTitle());
                msg.setContent(notice.getContent());
                msg.setRelatedId(notice.getId());
                msg.setIsRead(0);
                return msg;
            }).collect(Collectors.toList());

            if (!messages.isEmpty()) {
                sysUserMessageService.saveBatch(messages);
            }

            // Broadcast via WebSocket
            WebSocketServer.broadcastMessage("New notice published: " + notice.getTitle());
        }
    }

    @PostMapping
    public Result<?> createNotice(@RequestBody SysNotice sysNotice) {
        sysNoticeService.save(sysNotice);
        handleNoticePublished(sysNotice);
        return Result.success("公告创建成功");
    }

    @GetMapping
    public Result<IPage<SysNotice>> listNotices(@RequestParam(defaultValue = "1") Integer current,
                                                @RequestParam(defaultValue = "10") Integer size) {
        Page<SysNotice> page = new Page<>(current, size);
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(SysNotice::getCreatedAt);
        IPage<SysNotice> noticePage = sysNoticeService.page(page, wrapper);
        return Result.success(noticePage);
    }
    
    @GetMapping("/active")
    public Result<List<SysNotice>> getActiveNotices() {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysNotice::getStatus, 1)
               .orderByDesc(SysNotice::getCreatedAt);
        List<SysNotice> notices = sysNoticeService.list(wrapper);
        return Result.success(notices);
    }

    @PutMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestBody SysNotice sysNotice) {
        SysNotice notice = sysNoticeService.getById(id);
        if (notice == null) {
            return Result.error("公告不存在");
        }
        Integer oldStatus = notice.getStatus();
        notice.setStatus(sysNotice.getStatus());
        sysNoticeService.updateById(notice);

        if ((oldStatus == null || oldStatus != 1) && sysNotice.getStatus() != null && sysNotice.getStatus() == 1) {
            handleNoticePublished(notice);
        }

        return Result.success("状态更新成功");
    }
    
    @DeleteMapping("/{id}")
    public Result<?> deleteNotice(@PathVariable Long id) {
        sysNoticeService.removeById(id);
        return Result.success("删除成功");
    }
}
