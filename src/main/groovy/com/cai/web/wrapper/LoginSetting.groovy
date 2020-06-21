package com.cai.web.wrapper

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "login", name = "maxStillState")
@ConfigurationProperties(prefix = "login")
class LoginSetting {

    String maxStillState = "0"

    String maxReuse = "0"
}
