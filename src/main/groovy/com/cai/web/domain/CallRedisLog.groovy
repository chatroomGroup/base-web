package com.cai.web.domain

import com.cai.general.util.log.Log

class CallRedisLog extends Log{

    CallRedisLog(String domain, Object response) {
        this.domain = domain
        this.response = response
    }

    String domain

    Object response

}
