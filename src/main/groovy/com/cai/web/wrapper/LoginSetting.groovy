package com.cai.web.wrapper

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "login", name = "maxStillState")
@ConfigurationProperties(prefix = "login")
class LoginSetting {

    long maxStillState

    long maxReuse
}
