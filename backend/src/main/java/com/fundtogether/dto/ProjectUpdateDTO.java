package com.fundtogether.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectUpdateDTO extends ProjectCreateDTO {
    @NotNull(message = "项目ID不能为空")
    private Long id;
}
