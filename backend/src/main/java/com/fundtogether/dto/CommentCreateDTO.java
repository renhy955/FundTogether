package com.fundtogether.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateDTO {
    @NotNull(message = "项目ID不能为空")
    private Long projectId;
    
    private Long parentId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
}