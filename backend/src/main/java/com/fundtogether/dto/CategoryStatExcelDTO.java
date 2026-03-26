package com.fundtogether.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CategoryStatExcelDTO {

    @ExcelProperty("分类名称")
    private String categoryName;

    @ExcelProperty("项目数量")
    private Long projectCount;

    @ExcelProperty("累计金额")
    private BigDecimal totalAmount;
}
