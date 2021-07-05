package com.xxxx.crm.controller;

import com.xxxx.crm.annotations.RequirePermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Autowired
    private SaleChanceService saleChanceService;

    @Autowired
    private UserService userService;


    /**
     * 多条件分页查询营销机会
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "101001")
    public Map<String,Object> querySaleChanceByParams(SaleChanceQuery query,Integer flag,HttpServletRequest request){
        // 查询参数 flag=1 代表当前查询为开发计划数据，设置查询分配人 参数
        System.out.println("test");//test
        if(flag!=null&&flag==1){
            // 获取当前登录用户的ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            query.setAssignMan(userId);
        }
        return saleChanceService.querySaleChanceByParams(query);
    }

    /**头部工具栏
     * 添加数据功能
     *
     * @param saleChance
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSaleChance(HttpServletRequest request,SaleChance saleChance){
//        对于机会数据添加与更新表单页可以实现共享，
//        这里在转发机会数据添加与更新页面时共用一套代码即可
//        (考虑更新时涉及到机会数据显示操作，这里根据机会id查询机会记录并放入到请求域中)。
        // 获取用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 获取用户的真实姓名
        String trueName = userService.selectByPrimaryKey(userId).getTrueName();
        // 设置营销机会的创建人
        saleChance.setCreateMan(trueName);
        saleChanceService.saveSaleChance(saleChance);
        //BaseController中的方法
        return success("营销机会数据添加成功！");
    }

    /**行内工具栏
     * 营销机会修改
     * @param saleChance
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo sayUpdate(SaleChance saleChance){
        saleChanceService.changeSaleChance(saleChance);
        return success("营销机会修改成功");
    }

    //多条件分页查询营销机会



    /**
     * 跳转页面
     * @return
     */

    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }


    /**
     * 机会数据添加与更新表单页面视图转发
     * id为空 添加操作
     * id非空 修改操作
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdate(Integer id, Model model){
        //修改和添加主要的区别是表单中是否有ID,有id修改操作，否则添加
        if(id != null){
            //查询销售机会对象
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            model.addAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

    @RequestMapping("dels")
    @ResponseBody
    public ResultInfo sayDels(Integer[] ids) {
        System.out.println(Arrays.toString(ids)+"<<<controller");
        saleChanceService.removeSaleChance(ids);
        return success("删除营销机会成功");
    }
}
