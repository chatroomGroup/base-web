package com.cai.web.controller

import com.cai.general.core.App
import com.cai.general.core.Session
import com.cai.general.util.encode.PasswordUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.general.util.session.SessionUtils
import com.cai.redis.RedisLockService
import com.cai.web.core.IgnoreAuth
import com.cai.web.core.IgnoreAuthStore
import com.cai.web.core.ReturnToken
import com.cai.web.core.log.LoginHistory
import com.cai.web.core.log.SignOutHistory
import com.cai.web.dao.UserMapper
import com.cai.web.domain.OnlineUserDomain
import com.cai.web.domain.Status
import com.cai.web.domain.User
import com.cai.web.domain.UserPassword
import com.cai.web.message.WebMessage
import com.cai.web.service.LoginService
import com.cai.web.service.UserPasswordService
import com.cai.web.wrapper.LoginSetting
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.util.concurrent.atomic.AtomicReference

import static com.cai.general.util.session.SessionUtils.createSession
import static com.cai.general.util.session.SessionUtils.destroySession
import static com.cai.general.util.session.SessionUtils.saveSession

@RestController
@RequestMapping("/rest/user")
class LoginController extends BaseController{

    @Autowired
    LoginService loginService

    @Autowired
    IgnoreAuthStore ignoreAuthStore

    @Autowired
    App app

    @Autowired
    LoginHistory loginHistory

    @Autowired
    SignOutHistory signOutHistory

    @IgnoreAuth
    @ReturnToken
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    ResponseMessage login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map params) {
        //账号验证
        ResponseMessage rsp = validateMapParamsNotNull(params)
        if (!rsp.isSuccess)
            return rsp

        String account = params.get("account")
        String password = params.get("password")
        rsp = loginService.login(null, account, password)
        // 第一次登陆 需要创建session
        if (rsp.isSuccess){
            String token = ignoreAuthStore.returnToken(request.getServletPath())
            saveSession(request, createSession(account, token, app.name, null, null, null))
            Session sess = getSession(request)
            loginHistory.logProcess(sess)
        }
        return rsp
    }

    @RequestMapping(path = "/login2")
    ResponseMessage test2(HttpServletRequest request){
        println getSession(request)
        return ResponseMessageFactory.success("login")
    }

    @RequestMapping(path = "/signOut")
    ResponseMessage signOut(HttpServletRequest request){
        Session session = getSession(request)
        loginService.destroyCache(new OnlineUserDomain(session.user, new AtomicReference<String>(session.token)))
        signOutHistory.logProcess(session, session.user)
        destroySession(request)
        return ResponseMessageFactory.success("signOut")
    }
}
