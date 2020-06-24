package com.cai.web.service

import com.cai.web.domain.ErrorStatusWrapper
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ErrorService {

    String path

    HttpServletRequest request

    HttpServletResponse response

    ErrorService createErrorForward(ErrorMapping path, HttpServletRequest request, HttpServletResponse response){
        this.path = path.getPath()
        this.request = request
        this.response = response
        return this
    }

    void forward(ErrorStatusWrapper wrapper){
        request.setAttribute("errorSts", wrapper)
        request.getRequestDispatcher(path).forward(request, response)
    }

}


enum ErrorMapping{

    error4xx("/error4xx")

    private String path

    ErrorMapping(String path) {
        this.path = path
    }

    String getPath(){
        return path
    }
}