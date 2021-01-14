package com.cai.web.controller

import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.cai.web.core.IgnoreAuth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = "/rest/upload")
class UploadController {

    @Autowired
    MongoService mongoService

    @IgnoreAuth
    @RequestMapping(value = "/{parent}/{fileName}")
    ResponseMessage upload(HttpServletRequest request, @PathVariable String parent, @PathVariable String fileName){
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFileMap()
        files.each {it->
            mongoService.gridFsUploadStream(it.getInputStream(), fileName, parent, null)
        }
        return ResponseMessageFactory.success()
    }
}
