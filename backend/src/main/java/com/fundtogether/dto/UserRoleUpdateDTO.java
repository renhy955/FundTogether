package com.fundtogether.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRoleUpdateDTO {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "角色不能为空")
    private Integer role; // 1-普通支持者 2-项目发起人 3-管理员
}
