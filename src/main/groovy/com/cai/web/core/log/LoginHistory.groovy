package com.cai.web.core.log

import com.cai.general.core.App
import com.cai.general.core.Session
import com.cai.general.util.async.AsyncUtils
import com.cai.general.util.log.LogParser
import com.cai.mongo.service.MongoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LoginHistory {

    @Autowired
    App app

    @Autowired
    LogParser logParser

    void logProcess(Session sess){
        if (!sess.user)
            return
        AsyncUtils.asyncOnlyPool(new AsyncLoginLogAction(), [logParser: logParser, user: sess.user, token: sess.token?:"", app: app.name?:""])
    }
}
