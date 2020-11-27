package com.cai.web.core.log

import com.cai.general.util.async.SerialAction
import com.cai.general.util.log.LogParser
import com.cai.mongo.service.MongoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

class AsyncLoginLogAction implements SerialAction {

    static String LOGIN_LOG_COLLECTION = "user_login"

    @Override
    Object action(Map<String, Object> map) {
        Map params = map

        LoginLog log = new LoginLog(LOGIN_LOG_COLLECTION)
        log.user = params.user
        log.app = params.app
        log.token = params.token

        LogParser logParser = params.logParser as LogParser
        logParser.insertLog(log)
        return null
    }
}
