package com.cai.web.core

import com.cai.web.domain.ErrorStatusWrapper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ErrorStatusBuilder {

    static ErrorStatusWrapper builder(String msg, String status, String path){
        return new ErrorStatusWrapper(path, msg, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), status)
    }
}
