package com.xxxx.crm.interceptors;

import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ⾮法访问拦截
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;
    /**
     * 判断⽤户是否是登录状态
     * 获取Cookie对象，解析⽤户ID的值
     * 如果⽤户ID不为空，且在数据库中存在对应的⽤户记录，表示请求合法
     * 否则，请求不合法，进⾏拦截，重定向到登录⻚⾯
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取Cookie中的⽤户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 判断⽤户ID是否不为空，且数据库中存在对应的⽤户记录
        if(null==userId || null==userService.selectByPrimaryKey(userId)){
            // 抛出未登录异常
            throw new NoLoginException();
        }
        return true;
    }
}
