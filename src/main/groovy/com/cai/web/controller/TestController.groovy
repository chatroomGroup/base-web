package com.cai.web.controller

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.web.core.IgnoreAuth
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @IgnoreAuth
    @RequestMapping("/login")
    ResponseMessage test(){
        return ResponseMessageFactory.success("login")
    }

    @IgnoreAuth
    @RequestMapping(path = "/login2")
    ResponseMessage test2(){
        return ResponseMessageFactory.success("login")
    }
}
