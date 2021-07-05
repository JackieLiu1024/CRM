package com.xxxx.crm.mapper;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    //根据roleId查询权限的数量
    Integer countPermissionByRoleId(Integer roleId);

    //根据roleId删除其所有的权限信息
    Integer deletePermissionsByRoleId(Integer roleId);

    //根据⻆⾊id 查询⻆⾊拥有的菜单id List<Integer>
    List<Integer> queryRoleHasAllModuleIdsByRoleId(Integer roleId);

    List<String> queryUserHasRolesHasPermissions(Integer userId);
}