package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    //用户登录
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo sayLogin(String userName,String userPwd){
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.doLogin(userName, userPwd);
        resultInfo.setResult(userModel);
        return resultInfo;
    }

    //修改密码
    @PostMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassWord, String newPassWord, String confirmPassword){
        //从Coolie中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        userService.updateUserPassWord(userId,oldPassWord,newPassWord,confirmPassword);
        return success("修改密码成功");
    }

    //基本资料  修改
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo sayUpdate(User user){
        userService.updateUser(user);
        return success("保存信息成功");
    }

    //用户管理模块
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryUserByParams(UserQuery userQuery){
        return userService.queryUserByParams(userQuery);
    }

    //添加用户
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user){
        userService.saveUser(user);
        return success("⽤户添加成功！");
    }

//    //更新用户
//    @RequestMapping("updateUser")
//    @ResponseBody
//    public ResultInfo updateUser(User user){
//        userService.updateUser(user);
//        return success("⽤户更新成功！");
//    }

    //删除用户
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUserByIds(ids);
        return success("⽤户记录删除成功!");
    }

    //查询所有的销售人员
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }


    /*跳转页面*/

    //跳转到修改密码页面
    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    //跳转到基本资料页面
    @RequestMapping("toSettingPage")
    public String toSettingPage(HttpServletRequest request){
        //从当前Cookie中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //根据userId查询用户信息
        User user = userService.selectByPrimaryKey(userId);
        //存储
        request.setAttribute("user",user);
        return "user/setting";
    }

    //跳转到用户信息页面
    @RequestMapping("index")
    public String index(){
        return "user/user";
    }

    //跳转到用户添加或更新页面
    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdateUserPage(Integer id, Model model){
        if(id!=null){
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }

}
