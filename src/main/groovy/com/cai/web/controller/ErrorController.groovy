package com.cai.web.controller

import com.cai.web.domain.ErrorStatusWrapper
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class ErrorController {

    @RequestMapping(value = "error4xx", produces = MediaType.TEXT_HTML_VALUE)
    String error4xxHtml(Model model, HttpServletRequest request, HttpServletResponse response){
        ErrorStatusWrapper wrapper = request.getAttribute("errorSts") as ErrorStatusWrapper
        model.addAttribute("error", wrapper)
        response.setStatus(wrapper.status as Integer)
        return "error4xx"
    }
}
