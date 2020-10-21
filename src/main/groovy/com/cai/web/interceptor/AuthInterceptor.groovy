package com.cai.web.interceptor

import com.cai.general.core.App
import com.cai.general.core.Session
import com.cai.general.util.log.ErrorLogManager
import com.cai.redis.RedisService
import com.cai.redis.op.OpJedis
import com.cai.web.core.ErrorStatusBuilder
import com.cai.web.core.IgnoreAuthStore
import com.cai.web.core.MappingWrapper
import com.cai.web.domain.ErrorStatusWrapper
import com.cai.web.domain.OnlineUserDomain
import com.cai.web.message.WebMessage
import com.cai.web.service.ErrorMapping
import com.cai.web.service.ErrorService
import com.cai.web.service.LoginService
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicReference
import static com.cai.general.util.session.SessionUtils.*
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

    @Autowired
    ErrorService errorService

    @Autowired
    App app

    @Autowired
    LoginService lgSvc
    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (webSetting.isLoose)
            return true
        if (ignoreAuthStore.hasMapping(request.getServletPath()))
            return true
        String user = request.getHeader("x-user")
        String token = request.getHeader("x-token")
        String app = request.getHeader("x-app")?:app.name
        String locale = request.getHeader("x-locale")?:"zh"
        String client = request.getHeader("x-client")?:getIPAddress(request)
        String port = request.getHeader("x-port")?:request.getRemotePort()
        try{
            return redisService.tryAndGetOpJedis{op->
                if (!user || !token){
                    ErrorStatusWrapper wrapper = ErrorStatusBuilder.builder(WebMessage.ERROR.MSG_ERROR_0001, HttpServletResponse.SC_UNAUTHORIZED as String, request.getServletPath())
                    errorService.createErrorForward(ErrorMapping.error4xx, request, response).forward(wrapper)
                    return false
                }
                String userStr = op.get(OnlineUserDomain.getAuthCacheKey(user, token) as String)
                if (!userStr){
                    ErrorStatusWrapper wrapper = ErrorStatusBuilder.builder(WebMessage.ERROR.MSG_ERROR_0002, HttpServletResponse.SC_UNAUTHORIZED as String, request.getServletPath())
                    errorService.createErrorForward(ErrorMapping.error4xx, request, response).forward(wrapper)
                    return false
                }
//        OnlineUserDomain userDomain = RedisService.unSerialize(userStr, OnlineUserDomain)
                if (!op.get(OnlineUserDomain.getTimeoutCacheKey(user, token) as String)){
                    op.del(OnlineUserDomain.getAuthCacheKey(user, token) as String)
                    ErrorStatusWrapper wrapper = ErrorStatusBuilder.builder(WebMessage.ERROR.MSG_ERROR_0004, HttpServletResponse.SC_REQUEST_TIMEOUT as String, request.getServletPath())
                    errorService.createErrorForward(ErrorMapping.error4xx, request, response).forward(wrapper)
                    return false
                }
                if (op.get(OnlineUserDomain.getAccessCacheKey(user, token) as String)){
                    ErrorStatusWrapper wrapper = ErrorStatusBuilder.builder(WebMessage.ERROR.MSG_ERROR_0003, HttpServletResponse.SC_UNAUTHORIZED as String, request.getServletPath())
                    errorService.createErrorForward(ErrorMapping.error4xx, request, response).forward(wrapper)
                    return false
                }
                saveSession(request, createSession(user, token, app, client, port, locale))
                return true
            }
        }catch(Throwable t){
            Session sess = new Session()
            sess.setUser(user)
            sess.setToken(token)
            t.printStackTrace()
            ErrorLogManager.logException(sess ,t)
        }
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
        if (ignoreAuthStore.hasMapping(request.getServletPath())){
            MappingWrapper wrapper = ignoreAuthStore.getMapping(request.getServletPath())
            if (wrapper.isReturnToken){
                Session sess = request.getAttribute("sess")
                if (!sess)  return
                String token = sess.token?:ignoreAuthStore.returnToken(request.getServletPath())
                response.addCookie(new Cookie("x-token", sess.token))
                response.addCookie(new Cookie("x-user", sess.user))
                response.addCookie(new Cookie("x-app", sess.app?:app.name))
                lgSvc.toCache(new OnlineUserDomain(sess.user, new AtomicReference<String>(token)))
            }
        }

    }

    @Override
    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex)
    }

    static String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
    }
}
