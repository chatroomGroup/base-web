package com.cai.web.core.log

import com.cai.general.util.log.Log

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LoginLog extends Log{

    long id = 0

    String user

    String app

    String token

    String loginTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

    String createdBy = "api"

    String created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

    LoginLog(String logName) {
        super(logName)
    }
}
