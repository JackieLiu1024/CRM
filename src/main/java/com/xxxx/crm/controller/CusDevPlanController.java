package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Autowired
    private CusDevPlanService cusDevPlanService;

    @Autowired
    private SaleChanceService saleChanceService;

    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    @RequestMapping("toCusDevPlanDataPage")
    public String sayList(Integer sid, Model model){
        //查询
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        //存储数据到作用域
        model.addAttribute("saleChance",saleChance);
        //转发
        return "cusDevPlan/cus_dev_plan_data";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> sayList(CusDevPlanQuery query){

        return cusDevPlanService.queryCusDevPlan(query);
    }


}
