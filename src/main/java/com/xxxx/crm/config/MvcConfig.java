package com.xxxx.crm.config;

import com.xxxx.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry){
        // 需要⼀个实现HandlerInterceptor接⼝的拦截器实例，这⾥使⽤的是NoLoginInterceptor
        registry.addInterceptor(noLoginInterceptor())
                // ⽤于设置拦截器的过滤路径规则
                .addPathPatterns("/**")
                // ⽤于设置不需要拦截的过滤规则
                .excludePathPatterns("/index","/user/login","/welcome","/css/**","/images/**","/js/**","/lib/**");
    }
}
