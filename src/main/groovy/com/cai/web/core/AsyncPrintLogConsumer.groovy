package com.cai.web.core

import com.cai.general.util.log.Log
import com.cai.general.util.log.LogParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Primary
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Primary
@Component
class AsyncPrintLogConsumer extends LogParser{

    Logger logger = LoggerFactory.getLogger(AsyncPrintLogConsumer)


    @Override
    def <T extends Log> void insertLog(T t) {
        logger.error(t.toString())
    }

    @Async
    @EventListener(Log)
    void process(Log log){
        insertLog(log)
    }

}
