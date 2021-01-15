package com.cai.web.controller

import com.cai.general.util.log.ErrorLogManager
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.web.core.IgnoreAuth
import com.cai.web.core.IgnoreAuthStore
import com.cai.web.message.WebMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping(value = "/rest/upload")
class UploadController {

    @Autowired
    MongoService mongoService

    @Autowired
    IgnoreAuthStore authStore

    @IgnoreAuth
    @RequestMapping(value = "/{parent}", method = RequestMethod.POST)
    ResponseMessage upload(HttpServletRequest request, @PathVariable String parent){
//        MultipartHttpServletRequest mulReq=(MultipartHttpServletRequest)request
        try{
            Map<String,MultipartFile> files = ((MultipartHttpServletRequest) request).getFileMap()
            files.each {k,v->
                mongoService.gridFsUploadStream(v.getInputStream(), k, parent, null)
            }
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            ErrorLogManager.logException(null, t)
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0000)
        }

    }

    @IgnoreAuth
    @RequestMapping(value = "/{db}/{parent}", method = RequestMethod.POST)
    ResponseMessage uploadByDB(HttpServletRequest request, @PathVariable String db, @PathVariable String parent){
//        MultipartHttpServletRequest mulReq=(MultipartHttpServletRequest)request
        try{
            Map<String,MultipartFile> files = ((MultipartHttpServletRequest) request).getFileMap()
            files.each {k,v->
                mongoService.gridFsUploadStream(db, v.getInputStream(), k, parent, null)
            }
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            ErrorLogManager.logException(null, t)
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0000)
        }

    }

    @IgnoreAuth
    @RequestMapping(value = "/ignoreAuth", method = RequestMethod.GET)
    ResponseMessage ignoreAuthMap(HttpServletRequest request){
//        MultipartHttpServletRequest mulReq=(MultipartHttpServletRequest)request
        return ResponseMessageFactory.success("success", authStore.getStore())
    }

}
