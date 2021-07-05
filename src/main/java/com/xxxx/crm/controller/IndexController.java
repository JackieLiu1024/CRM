package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.PermissionService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;
    /**
     * 登录页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    /**
     * 欢迎页面
     * @return
     */
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 后台页面
     * @return
     */
    @RequestMapping("main")
    //在 IndexController控制器中，main ⽅法转发时，查询登录⽤户信息并放置到 request 域。
    public String main(HttpServletRequest request){
        // 通过⼯具类，从cookie中获取userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 调⽤对应Service层的⽅法，通过userId主键查询⽤户对象
        User user = userService.selectByPrimaryKey(userId);
        // 将⽤户对象设置到request作⽤域中
        request.setAttribute("user",user);
        //查询当前用户拥有的权限
        List<String> permissions=permissionService.queryUserHasRolesHasPermissions(userId);
        //存储到session
        request.getSession().setAttribute("permissions",permissions);
        return "main";
    }
}
