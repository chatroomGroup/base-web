package com.cai.web.controller

import com.cai.general.core.BaseController
import com.cai.general.util.response.ResponseMessage
import com.cai.web.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/rest/user")
class UserController extends BaseController{

    @Autowired
    UserService usSvc

    @RequestMapping(path = "/destroy/{account}", method = RequestMethod.DELETE)
    ResponseMessage destroyUser(HttpServletRequest request, @PathVariable String account){
        return usSvc.destroyUserInfo(getSession(request), account)
    }
}
