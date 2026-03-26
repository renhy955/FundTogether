package com.fundtogether.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProjectCreateDTO {
    
    @NotBlank(message = "项目标题不能为空")
    private String title;
    
    @NotBlank(message = "项目简介不能为空")
    private String summary;
    
    @NotBlank(message = "项目封面不能为空")
    private String coverImage;
    
    private String videoUrl;
    
    @NotBlank(message = "项目详情不能为空")
    private String content;
    
    @NotNull(message = "目标金额不能为空")
    private BigDecimal targetAmount;
    
    @NotNull(message = "截止时间不能为空")
    private LocalDateTime endTime;
    
    private Long categoryId;
}
