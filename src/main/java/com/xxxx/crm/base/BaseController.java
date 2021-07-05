package com.xxxx.crm.base;


import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    //@ModelAttribute   注解注释的方法会在此controller每个方法执行前被执行
    @ModelAttribute
    public void preHandler(HttpServletRequest request){
        //getContextPath()获取当前项目名
        request.setAttribute("ctx", request.getContextPath());
    }


    public ResultInfo success(){
        return new ResultInfo();
    }

    public ResultInfo success(String msg){
        ResultInfo resultInfo= new ResultInfo();
        resultInfo.setMsg(msg);
        return resultInfo;
    }

    public ResultInfo success(String msg,Object result){
        ResultInfo resultInfo= new ResultInfo();
        resultInfo.setMsg(msg);
        resultInfo.setResult(result);
        return resultInfo;
    }

}
