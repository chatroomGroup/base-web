package com.cai.web.core.log.consumer

import com.cai.general.util.log.LogParser
import com.cai.reactor.annotataion.BindRule
import com.cai.reactor.core.Consumer
import com.cai.reactor.core.Event
import com.cai.reactor.core.SimpleRule
import com.cai.web.core.log.SignOutLog
import org.springframework.stereotype.Component

@Component
@BindRule(rules = [SimpleRule])
class SignOutLogConsumer implements Consumer{

    static String LOGIN_LOG_COLLECTION = "user_sign_out"

    @Override
    String key() {
        return "user.sign.out"
    }

    @Override
    void consume(Event event) {
        def params = event.getParams()
        SignOutLog log = new SignOutLog(LOGIN_LOG_COLLECTION)
        log.user = params.user
        log.app = params.app
        log.token = params.token
        log.operator = params.operator

        LogParser logParser = params.logParser as LogParser
        logParser.insertLog(log)
    }
}
