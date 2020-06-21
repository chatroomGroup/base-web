package com.cai.web.core

import com.cai.general.core.BaseEntity
import com.cai.general.util.response.ResponseMessage
import com.cai.redis.RedisService
import com.cai.web.wrapper.LoginSetting
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import org.springframework.web.context.support.GenericWebApplicationContext
import redis.clients.jedis.Jedis

import java.lang.reflect.Method
import java.security.MessageDigest
import java.text.MessageFormat

@Component
class CacheEntity{
    @Autowired
    LoginSetting loginSetting

}
