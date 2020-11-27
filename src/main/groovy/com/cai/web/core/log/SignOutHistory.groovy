package com.cai.web.core.log

import com.cai.general.core.App
import com.cai.general.core.Session
import com.cai.general.util.async.AsyncUtils
import com.cai.general.util.log.LogParser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SignOutHistory {

    @Autowired
    App app

    @Autowired
    LogParser logParser

    void logProcess(Session sess, String operator = "api"){
        if (!sess.user)
            return
        AsyncUtils.asyncOnlyPool(new AsyncSignOutLogAction(), [logParser: logParser, operator: operator, user: sess.user, token: sess.token?:"", app: app.name?:""])
    }
}
