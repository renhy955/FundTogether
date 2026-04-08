package com.fundtogether.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fundtogether.entity.SupportOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface SupportOrderMapper extends BaseMapper<SupportOrder> {

    @Select("SELECT IFNULL(SUM(amount), 0) FROM support_order WHERE status = 1 AND deleted = 0")
    BigDecimal getTotalAmount();

    @Select("SELECT IFNULL(SUM(amount), 0) FROM support_order WHERE user_id = #{userId} AND status = 1 AND deleted = 0")
    BigDecimal getUserTotalSupportAmount(Long userId);

    @Select("SELECT DATE_FORMAT(pay_time, '%Y-%m') as month, SUM(amount) as totalAmount " +
            "FROM support_order " +
            "WHERE status = 1 AND deleted = 0 AND pay_time >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 5 MONTH), '%Y-%m-01') " +
            "GROUP BY month " +
            "ORDER BY month ASC")
    java.util.List<java.util.Map<String, Object>> getFundingTrend();
}

