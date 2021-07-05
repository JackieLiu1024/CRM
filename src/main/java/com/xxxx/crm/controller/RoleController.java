package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.RoleQuery;
import com.xxxx.crm.service.RoleService;
import com.xxxx.crm.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    //查询⻆⾊列表
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }

    //index
    @RequestMapping("index")
    public String sayIndex(){
        return "role/role";
    }

    //list列表查询
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> userList(RoleQuery roleQuery){
        return roleService.queryByParamsForTable(roleQuery);
    }

    //跳转弹出层add_update
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateRolePage(Integer id, Model model){
        if(null !=id){
            model.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }

    //添加角色
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.saveRole(role);
        return success("⻆⾊记录添加成功");
    }

    //更新角色
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("⻆⾊记录更新成功");
    }

    //假删角色
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("⻆⾊记录删除成功");
    }

    //跳转授权页面
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    //授权
    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mids,Integer roleId){
        roleService.addGrant(mids,roleId);
        return success("权限修改成功!!!");
    }
}
