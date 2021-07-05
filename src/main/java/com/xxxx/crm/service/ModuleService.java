package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dto.TreeDto;
import com.xxxx.crm.mapper.ModuleMapper;
import com.xxxx.crm.mapper.PermissionMapper;
import com.xxxx.crm.vo.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    /*//查询所有的模块
    public List<TreeDto> queryAllModules(){
        return moduleMapper.queryAllModules();
    }*/

    public List<TreeDto> queryAllModules(Integer roleId) {
        //查询所有的资源
        List<TreeDto> treeDtos=moduleMapper.queryAllModules();
        // 根据⻆⾊id 查询⻆⾊拥有的菜单id List<Integer>
        List<Integer> roleHasMids=permissionMapper.queryRoleHasAllModuleIdsByRoleId(roleId);
        //循环遍历，判断角色拥有那些资源，checked=true,checked=false;
        if(null !=roleHasMids && roleHasMids.size()>0){
            for(TreeDto treeDto:treeDtos){
                if(roleHasMids.contains(treeDto.getId())){
                    treeDto.setChecked(true);
                }
            }
        }
        /*if(null !=roleHasMids && roleHasMids.size()>0){
            treeDtos.forEach(treeDto -> {
                if(roleHasMids.contains(treeDto.getId())){
                    // 说明当前⻆⾊ 分配了该菜单
                    treeDto.setChecked(true);
                }
            });
        }*/

        return treeDtos;
    }
}
