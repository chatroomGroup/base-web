package com.cai.web.controller

import com.cai.general.util.log.ErrorLogManager
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.web.core.IgnoreAuth
import com.cai.web.message.WebMessage
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping(value = "/rest/download")
class DownloadController {


    @Autowired
    MongoService mongoService

    @IgnoreAuth
    @RequestMapping(value = "/{parent}/{name}", method = RequestMethod.GET)
    String download(HttpServletRequest request, HttpServletResponse response, @PathVariable String parent , @PathVariable String name){
        InputStream is
        OutputStream os
        try{
            response.setHeader("Content-Disposition","attachment; filename=\"$name\"")
            is = mongoService.gridFsFindFileByName(name, parent)
            os = response.outputStream
            IOUtils.copy(is, os)
            return null
        }catch(Throwable t){
            ErrorLogManager.logException(null, t)
            return null
        }finally{
            is?.close()
            os?.close()
        }

    }

    @IgnoreAuth
    @RequestMapping(value = "/{db}/{parent}/{name}", method = RequestMethod.GET)
    String downloadByDB(HttpServletRequest request, HttpServletResponse response, @PathVariable String db , @PathVariable String parent , @PathVariable String name){
        InputStream is
        OutputStream os
        try{
            response.setHeader("Content-Disposition","attachment; filename=\"$name\"")
            is = mongoService.gridFsFindFileByName(db, name, parent)
            os = response.outputStream
            IOUtils.copy(is, os)
            return null
        }catch(Throwable t){
            ErrorLogManager.logException(null, t)
            return null
        }finally{
            is?.close()
            os?.close()
        }

    }
}
