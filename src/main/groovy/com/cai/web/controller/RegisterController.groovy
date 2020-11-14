package com.cai.web.controller

import com.cai.general.core.BaseController
import com.cai.general.util.response.ResponseMessage
import com.cai.web.core.IgnoreAuth
import com.cai.web.service.RegisterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/rest/user")
class RegisterController extends BaseController{

    @Autowired
    RegisterService registerService

    @IgnoreAuth
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    ResponseMessage login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map params) {
        ResponseMessage rsp = validateMapParamsNotNull(params)
        if (!rsp.isSuccess)
            return rsp
        return registerService.register(params.account as String, params.name as String, params.password as String)
    }

}
