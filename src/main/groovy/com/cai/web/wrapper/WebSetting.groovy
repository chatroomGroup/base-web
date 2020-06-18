package com.cai.web.wrapper

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "web", name = "accessInterval")
@ConfigurationProperties(prefix = "web")
class WebSetting {
    long accessInterval = 300

    boolean isTest = false
}
