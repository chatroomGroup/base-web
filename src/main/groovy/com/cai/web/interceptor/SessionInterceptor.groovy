package com.cai.web.interceptor


import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SessionInterceptor extends HandlerInterceptorAdapter{

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String user = request.getHeader("x-user")?request.getHeader("x-user"):"unknown"
        String token = request.getHeader("x-token")
        request.setAttribute("user", user)
        request.setAttribute("token", token)
        return true
    }
}
