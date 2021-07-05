package com.xxxx.crm.aop;

import com.xxxx.crm.annotations.RequirePermission;
import com.xxxx.crm.exceptions.NoAuthException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@Aspect
public class PermissionProxy {
    @Autowired
    private HttpSession session;
    @Around(value = "@annotation(com.xxxx.crm.annotations.RequirePermission)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //判断是否有权限
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        //判断
        if(null == permissions || permissions.size()==0){
            throw new NoAuthException();
        }
        //有权限，获取权限码
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        RequirePermission requirePermission = methodSignature.getMethod().getDeclaredAnnotation(RequirePermission.class);
        if(!(permissions.contains(requirePermission.code()))){
            throw new NoAuthException();
        }
        Object result= pjp.proceed();
        return result;
    }
}
