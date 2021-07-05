package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.mapper.ModuleMapper;
import com.xxxx.crm.mapper.PermissionMapper;
import com.xxxx.crm.mapper.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    //查询⻆⾊列表
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    //⻆⾊添加
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输⼊⻆⾊名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp,"该⻆⾊已存在!");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(role)<1,"⻆⾊记录添加失败!");
    }


    //角色更新
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        AssertUtil.isTrue(null==role.getId()||null==selectByPrimaryKey(role.getId()),"待修改的记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输⼊⻆⾊名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        //  !(temp.getId().equals(role.getId())--
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(role.getId())),"该⻆⾊已存在!");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"⻆⾊记录更新失败!");
    }

    //角色删除(假删)
    public void deleteRole(Integer roleId){
        Role temp =selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null==roleId||null==temp,"待删除的记录不存在!");
        temp.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"⻆⾊记录删除失败!");
    }

    //权限记录添加后端实现
    public void addGrant(Integer[] mids, Integer roleId) {
        /**
         * 核⼼表-t_permission t_role(校验⻆⾊存在)
         * 如果⻆⾊存在原始权限 删除⻆⾊原始权限
         * 然后添加⻆⾊新的权限 批量添加权限记录到t_permission
         */
        //验证角色role是否存在
        Role temp =selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null==roleId||null==temp,"待授权的⻆⾊不存在!");
        //根据roleId查询权限的数量
        int count = permissionMapper.countPermissionByRoleId(roleId);
        //角色的权限数量>0,则根据角色id(roleId)删除原有权限
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionsByRoleId(roleId)<count,"权限分配失败!");
        }
        //选中的模块id--mids
        if(null !=mids && mids.length>0){
            List<Permission> permissions=new ArrayList<Permission>();
            //遍历
            for (Integer mid : mids) {
                //设置值
                Permission permission=new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mid);//模块id
                permission.setRoleId(roleId);//角色
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());//设置 操作权限值
                //添加一跳权限
                permissions.add(permission);
            }
            //批量添加验证
            AssertUtil.isTrue(permissionMapper.insertBatch(permissions)!=permissions.size(),"授权失败");
        }
    }
}
