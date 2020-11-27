package com.cai.web.domain

import com.cai.general.util.log.Log

class CallRedisLog extends Log{


    CallRedisLog(String logName) {
        super(logName)
    }

    CallRedisLog(String logName, String domain, Object response) {
        super(logName)
        this.domain = domain
        this.response = response
    }

    String domain

    Object response

}
