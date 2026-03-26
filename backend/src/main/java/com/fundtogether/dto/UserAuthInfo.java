package com.fundtogether.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserAuthInfo {
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @NotBlank(message = "身份证号不能为空")
    private String idCard;

    @NotBlank(message = "身份证正面照片不能为空")
    private String idCardFront;

    @NotBlank(message = "身份证反面照片不能为空")
    private String idCardBack;
}