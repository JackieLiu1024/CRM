package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    //统计当前用户的角色的个数
    Integer countUserRoleByUserId(Integer uid);

    //删除当前用户的角色
    public Integer deleteUserRolesByUid(Integer uid);

    //批量添加


}