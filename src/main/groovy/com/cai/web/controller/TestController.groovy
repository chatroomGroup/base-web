package com.cai.web.controller

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.redis.RedisLockService
import com.cai.web.core.IgnoreAuth
import com.cai.web.core.IgnoreAuthStore
import com.cai.web.core.ReturnToken
import com.cai.web.dao.UserRepository
import com.cai.web.domain.OnlineUserDomain
import com.cai.web.domain.Status
import com.cai.web.domain.User
import com.cai.web.message.WebMessage
import com.cai.web.service.LoginService
import com.cai.web.wrapper.LoginSetting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
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

    @Autowired
    UserRepository userRep

    @Autowired
    RedisLockService rlSvc

    @IgnoreAuth
    @ReturnToken
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    ResponseMessage login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map params) {
        //
        //账号验证
        String account = params.get("account")
        String password = params.get("password")
        if (!account)
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0005)
        List<User> user = userRep.getUserByAccountAndPassword(account, password)
        if (!user || user.isEmpty())
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0006)
        if (user[0].status != Status.UserStatus.OPEN){
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0007)
        }
        //账号被锁定 相同账号是不被允许的
        if (user.size() > 1) {
            rlSvc.tryOpLock(user[0].getCacheKey(), {
                userRep.disableAccountStatusByIds(user.collect { it.id })
            })
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0007)
        }
        String token = ignoreAuthStore.returnToken(request.getServletPath())
        response.addCookie(new Cookie("x-token", token))
        response.addCookie(new Cookie("x-user", account))
        loginService.toCache(new OnlineUserDomain(account, new AtomicReference<String>(token)))
        return ResponseMessageFactory.success("login")
    }

    @RequestMapping(path = "/login2")
    ResponseMessage test2(){
        return ResponseMessageFactory.success("login")
    }
}
