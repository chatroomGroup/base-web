package com.cai.web.core.log

import com.cai.general.util.async.SerialAction
import com.cai.general.util.log.LogParser

class AsyncSignOutLogAction implements SerialAction {

    static String LOGIN_LOG_COLLECTION = "user_sign_out"

    @Override
    Object action(Map<String, Object> map) {
        Map params = map

        SignOutLog log = new SignOutLog(LOGIN_LOG_COLLECTION)
        log.user = params.user
        log.app = params.app
        log.token = params.token
        log.operator = params.operator

        LogParser logParser = params.logParser as LogParser
        logParser.insertLog(log)
        return null
    }
}
