package com.cai.web.core.log

import com.cai.general.core.App
import com.cai.general.core.Session
import com.cai.general.util.async.AsyncUtils
import com.cai.general.util.log.LogParser
import com.cai.mongo.service.MongoService
import com.cai.reactor.core.AbstractEvent
import com.cai.reactor.core.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LoginHistory {

    @Autowired
    App app

    @Autowired
    LogParser logParser

    @Autowired
    Publisher publisher

    void logProcess(Session sess){
        if (!sess.user)
            return
        SignInEvent event = new SignInEvent()
        event.setParams([logParser: logParser, user: sess.user, token: sess.token?:"", app: app.name?:""])
//        AsyncUtils.asyncOnlyPool(new AsyncLoginLogAction(), [logParser: logParser, user: sess.user, token: sess.token?:"", app: app.name?:""])
        publisher.publish(event)
    }
}

@Component
class SignInEvent extends AbstractEvent{

    @Override
    String topic() {
        return "user.sign.in"
    }

}