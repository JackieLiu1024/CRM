package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.SaleChance;

/*由于考虑到多个模块均涉及多条件查询 这里对于多条件分页查询方法由父接口BaseMapper定义 */
public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    /**
     * 新增
     */
}