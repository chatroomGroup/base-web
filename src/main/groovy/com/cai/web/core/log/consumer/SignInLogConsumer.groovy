package com.cai.web.core.log.consumer

import com.cai.general.util.log.LogParser
import com.cai.reactor.annotataion.BindRule
import com.cai.reactor.core.Consumer
import com.cai.reactor.core.Event
import com.cai.reactor.core.SimpleRule
import com.cai.web.core.log.LoginLog
import org.springframework.stereotype.Component

@Component
@BindRule(rules = [SimpleRule])
class SignInLogConsumer implements Consumer{

    static String LOGIN_LOG_COLLECTION = "user_login"

    @Override
    String key() {
        return "user.sign.in"
    }

    @Override
    void consume(Event event) {
        Map params = event.getParams()

        LoginLog log = new LoginLog(LOGIN_LOG_COLLECTION)
        log.user = params.user
        log.app = params.app
        log.token = params.token

        LogParser logParser = params.logParser as LogParser
        logParser.insertLog(log)
    }
}
