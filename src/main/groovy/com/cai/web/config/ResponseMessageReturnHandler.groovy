package com.cai.web.config

import com.alibaba.fastjson.JSON
import com.cai.general.util.log.ErrorLogManager
import com.cai.general.util.response.ResponseMessage
import org.springframework.core.MethodParameter
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.method.support.ModelAndViewContainer

import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

@Component
class ResponseMessageReturnHandler implements HandlerMethodReturnValueHandler {
    @Override
    boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getNestedParameterType() == ResponseMessage
    }

    @Override
    void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true)
        try {
            ServletResponse rsp = webRequest.getNativeResponse(HttpServletResponse)
            rsp.setContentType("text/json;charset=UTF-8")
            rsp.getWriter()
                .write(JSON.toJSONString(returnValue))
        }catch(IOException t){
            ErrorLogManager.logException(null, t)
            t.printStackTrace()
        }
    }
}
