package com.xxxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 全局异常处理
 */
@Component
public class GlobalExceptoinResolver implements HandlerExceptionResolver {

    /**
     * controller方法返回值类型
     *  视图
     *  JSON
     *  判断方法: 配置 @ResponseBody 注解为JSON对象,否则为视图
     * @param req
     * @param resp
     * @param handler
     * @param ex
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) {

        //未登录异常的处理
        if(ex instanceof NoLoginException){
            // 如果捕获的是未登录异常，则重定向到登录页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        //实例化对象
        ModelAndView mav=new ModelAndView("error");
        mav.addObject("code",400);
        mav.addObject("msg","系统正忙，稍后访问");

        /**
         * handler方法返回值有一下情况
         *
         *  JSON---resultInfo
         *
         *  String---view名
         *
         */

        if(handler instanceof HandlerMethod){
            //转换
            HandlerMethod handlerMethod =(HandlerMethod)handler;
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);

            //判断 ResponseBody 注解是否存在 (如果不存在，表示返回的是视图;如果存在，表示返回的是JSON)
            if(responseBody==null){
                //返回具体的试图名称
                if(ex instanceof ParamsException){
                    ParamsException pex = (ParamsException) ex;
                    mav.addObject("code",pex.getCode());
                    mav.addObject("msg",pex.getMsg());
                }
                return mav;
            }else {
                //返回JSON
                //ResultInfo  text/html;
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统正忙,,,,,请重试");

                if(ex instanceof ParamsException){
                    ParamsException pex=(ParamsException) ex;
                    resultInfo.setMsg(pex.getMsg());
                    resultInfo.setCode(pex.getCode());
                }

                resp.setContentType("application/json;charset=utf-8");

                PrintWriter out = null;
                try {
                    out = resp.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    //关闭输出流
                    if(out!=null){
                        out.close();
                    }
                }
                return null;
            }
        }
        return mav;
    }
}
