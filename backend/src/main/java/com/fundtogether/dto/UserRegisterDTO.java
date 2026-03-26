package com.fundtogether.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterDTO {
    
    @NotBlank(message = "注册方式不能为空")
    @Pattern(regexp = "^(phone|email)$", message = "注册方式只能是手机号(phone)或邮箱(email)")
    private String registerType;
    
    @NotBlank(message = "账号不能为空")
    private String account;
    
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
    
    @NotBlank(message = "验证码不能为空")
    private String code;
}
