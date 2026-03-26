package com.fundtogether.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPaymentMethodDTO {
    @NotNull(message = "支付类型不能为空")
    private Integer type; // 1-微信, 2-支付宝, 3-银行卡
    
    @NotBlank(message = "账号不能为空")
    private String account;
    
    private String name;
    
    private String bankName;
}
