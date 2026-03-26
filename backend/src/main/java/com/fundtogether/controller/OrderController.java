package com.fundtogether.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fundtogether.common.Result;
import com.fundtogether.dto.SupportOrderCreateDTO;
import com.fundtogether.entity.SupportOrder;
import com.fundtogether.security.LoginUser;
import com.fundtogether.service.SupportOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private SupportOrderService supportOrderService;

    private Long getCurrentUserId() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getId();
    }

    @PostMapping("/create")
    public Result<?> createOrder(@RequestBody @Valid SupportOrderCreateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        
        supportOrderService.createOrder(dto, getCurrentUserId());
        return Result.success("支持订单创建成功");
    }

    @GetMapping("/my-list")
    public Result<IPage<SupportOrder>> getMyOrders(@RequestParam(defaultValue = "1") Integer current,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        IPage<SupportOrder> page = supportOrderService.getMyOrders(getCurrentUserId(), current, size);
        return Result.success(page);
    }

    @GetMapping("/my-stats")
    public Result<?> getMyStats() {
        return Result.success(supportOrderService.getMyStats(getCurrentUserId()));
    }
}
