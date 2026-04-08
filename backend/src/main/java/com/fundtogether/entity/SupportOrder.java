package com.fundtogether.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("support_order")
public class SupportOrder {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    
    private Long userId;
    
    private Long projectId;
    
    private BigDecimal amount;
    
    private String message;
    
    /**
     * 0-待支付 1-已支付 2-已取消
     */
    private Integer status;
    
    private String payChannel;
    
    private LocalDateTime payTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
    
    @TableField(exist = false)
    private String projectName;
    
    @TableField(exist = false)
    private String userName;
}
