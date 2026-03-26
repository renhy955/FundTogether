package com.fundtogether.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserAuthInfoDTO {
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    
    @NotBlank(message = "身份证号不能为空")
    private String idCard;
    
    @NotBlank(message = "身份证正面不能为空")
    private String idCardFront;
    
    @NotBlank(message = "身份证反面不能为空")
    private String idCardBack;
}