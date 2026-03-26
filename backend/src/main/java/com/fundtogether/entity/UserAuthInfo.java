package com.fundtogether.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_auth_info")
public class UserAuthInfo {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String realName;
    
    private String idCard;
    
    private String idCardFront;
    
    private String idCardBack;
    
    /**
     * 审核状态: 0-待审核, 1-审核通过, 2-审核驳回
     */
    private Integer status;
    
    private String auditReason;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
}