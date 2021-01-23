package com.cai.web.core.log

import com.cai.general.core.App
import com.cai.general.core.Session
import com.cai.general.util.async.AsyncUtils
import com.cai.general.util.log.LogParser
import com.cai.reactor.core.AbstractEvent
import com.cai.reactor.core.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SignOutHistory {

    @Autowired
    App app

    @Autowired
    LogParser logParser

    @Autowired
    Publisher publisher

    void logProcess(Session sess, String operator = "api"){
        if (!sess.user)
            return
        SignOutEvent event = new SignOutEvent()
        event.setParams([logParser: logParser, operator: operator, user: sess.user, token: sess.token?:"", app: app.name?:""])
        publisher.publish(event)
//        改用reactor-core中方法解决异步事件
//        AsyncUtils.asyncOnlyPool(new AsyncSignOutLogAction(), [logParser: logParser, operator: operator, user: sess.user, token: sess.token?:"", app: app.name?:""])
    }
}

@Component
class SignOutEvent extends AbstractEvent{

    @Override
    String topic() {
        return "user.sign.out"
    }

}