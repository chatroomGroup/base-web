package com.cai.web.controller

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.redis.RedisService
import com.cai.web.core.IgnoreAuth
import com.cai.web.core.IgnoreAuthStore
import com.cai.web.core.ReturnToken
import com.cai.web.domain.OnlineUserDomain
import com.cai.web.service.LoginService
import com.cai.web.wrapper.LoginSetting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.concurrent.atomic.AtomicReference

@RestController
@RequestMapping("/test")
class TestController {

    @Autowired
    IgnoreAuthStore ignoreAuthStore

    @Autowired
    LoginService loginService


    @IgnoreAuth
    @ReturnToken
    @RequestMapping("/login")
    ResponseMessage test(HttpServletRequest request, HttpServletResponse response){
        //
         //账号验证
        //
        String token = ignoreAuthStore.returnToken(request.getServletPath())
        if (ignoreAuthStore.hasReturnToken(request.getServletPath())){
            response.addCookie(new Cookie("x-token", token))
            response.addCookie(new Cookie("x-user", 'test'))
            request.setAttribute("x-token", token)
            request.setAttribute("x-user", 'test')
            loginService.toCache(new OnlineUserDomain('test', new AtomicReference<String>(token)))
        }
        return ResponseMessageFactory.success("login")
    }

    @RequestMapping(path = "/login2")
    ResponseMessage test2(){
        return ResponseMessageFactory.success("login")
    }
}
