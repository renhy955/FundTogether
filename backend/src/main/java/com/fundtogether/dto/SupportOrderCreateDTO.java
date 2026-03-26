package com.fundtogether.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SupportOrderCreateDTO {
    
    @NotNull(message = "项目ID不能为空")
    private Long projectId;
    
    @NotNull(message = "支持金额不能为空")
    @DecimalMin(value = "0.01", message = "支持金额必须大于0")
    private BigDecimal amount;
    
    private String message;
    
    private String payChannel;
}
