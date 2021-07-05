package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    //新增
    User selectUserByName(String userName);

    //营销机会--添加|更新--指派给 下拉框
    public List<Map<String,Object>> queryAllSales();

}