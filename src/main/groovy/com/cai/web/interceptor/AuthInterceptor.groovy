package com.cai.web.interceptor

import com.cai.redis.RedisService
import com.cai.web.core.IgnoreAuthStore
import com.cai.web.domain.OnlineUserDomain
import com.cai.web.message.WebMessage
import com.cai.web.wrapper.LoginSetting
import com.cai.web.wrapper.WebSetting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.concurrent.atomic.AtomicReference

/**
 * 除了身份验证，还有访问间隔的验证
 */
@Component
class AuthInterceptor extends HandlerInterceptorAdapter{

    @Autowired
    RedisService redisService

    @Autowired
    WebSetting webSetting

    @Autowired
    IgnoreAuthStore ignoreAuthStore

    @Autowired
    LoginSetting loginSetting

    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (webSetting.isTest)
            return true
        if (ignoreAuthStore.hasMapping(request.getServletPath()))
            return true
        String user = request.getHeader("x-user")
        String token = request.getHeader("x-token")
        if (!user || !token){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, WebMessage.ERROR.MSG_ERROR_0001)
            return false
        }
        String userStr = redisService.jedis.get(OnlineUserDomain.getAuthCacheKey(user, token) as String)
        if (!userStr){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, WebMessage.ERROR.MSG_ERROR_0002)
            return false
        }
//        OnlineUserDomain userDomain = RedisService.unSerialize(userStr, OnlineUserDomain)
        if (!redisService.jedis.get(OnlineUserDomain.getTimeoutCacheKey(user, token) as String)){
            response.sendError(HttpServletResponse.SC_REQUEST_TIMEOUT, WebMessage.ERROR.MSG_ERROR_0004)
            return false
        }
        if (redisService.jedis.get(OnlineUserDomain.getAccessCacheKey(user, token) as String)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, WebMessage.ERROR.MSG_ERROR_0003)
            return false
        }
        return true
    }

//    @Override
//    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
//        OnlineUserDomain domain = new OnlineUserDomain(request.getAttribute("x-user") as String, new AtomicReference(request.getAttribute("x-token")))
//        redisService.getJedis().psetex(domain.getAccessCacheKey() as String, loginSetting.maxReuse as long, RedisService.serialize(domain))
//        redisService.getJedis().set(domain.getAuthCacheKey() as String, RedisService.serialize(domain))
//        redisService.getJedis().psetex(domain.getTimeoutCacheKey() as String, loginSetting.maxStillState as long, RedisService.serialize(domain))
//    }

    @Override
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView)
    }

    @Override
    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex)
    }
}