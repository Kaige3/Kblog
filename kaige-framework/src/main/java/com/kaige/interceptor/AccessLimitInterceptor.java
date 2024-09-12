package com.kaige.interceptor;

import com.kaige.annotation.AccessLimit;
import com.kaige.entity.Result;
import com.kaige.service.RedisService;
import com.kaige.utils.IpAddressUtils;
import com.kaige.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.kaige.annotation.AccessLimit;
import com.kaige.entity.Result;
import com.kaige.service.RedisService;
import com.kaige.utils.IpAddressUtils;
import com.kaige.utils.JacksonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Description: 访问控制拦截器
 * @Author: Naccl
 * @Date: 2021-04-04
 */
@Component
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 这是拦截器的核心方法，它在请求到达控制器之前执行。
         * 它的主要目的是通过 Redis 限制某个 IP 在特定时间段内的访问次数。
         */
//        判断当前的 handler 是否为一个方法处理器。拦截器有可能会处理非方法请求，
//        所以这里通过 instanceof 确保只拦截控制器中的方法。
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            从方法上获取 @AccessLimit 注解，
//            如果方法上没有标注这个注解，就直接放行，不做任何限制。
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            //方法上没有访问控制的注解，直接通过
            if (accessLimit == null) {
                return true;
            }
//             获取注解的属性
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();

//            生成 Redis Key
            String ip = IpAddressUtils.getIpAddress(request);
            String method = request.getMethod();
            String requestURI = request.getRequestURI();
            String redisKey = ip + ":" + method + ":" + requestURI;
//          检查访问次数
            Integer count = redisService.getObjectByValue(redisKey, Integer.class);
            if (count == null) {
                /**
                 * 如果 count == null，表示这是第一次访问：
                 * 调用 redisService.incrementByKey(redisKey, 1) 将访问次数设置为 1。
                 * 设置这个 Redis Key 的过期时间为 seconds，表示在指定时间内有效。
                 */
                //在规定周期内第一次访问，存入redis
                redisService.incrementByKey(redisKey, 1);
                redisService.expire(redisKey, seconds);
            } else {
                if (count >= maxCount) {
                    //超出访问限制次数
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    Result result = Result.create(403, accessLimit.msg());
                    out.write(JacksonUtils.writeValueAsString(result));
                    out.flush();
                    out.close();
                    return false;
                } else {
                    //没超出访问限制次数
                    redisService.incrementByKey(redisKey, 1);
                }
            }
        }
        return true;
    }
}
