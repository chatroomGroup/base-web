package com.cai.web.service

import com.cai.redis.RedisService
import com.cai.web.core.CacheEntity
import com.cai.web.core.CacheKey
import com.cai.web.domain.OnlineUserDomain
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis

import java.lang.reflect.Method
import java.text.MessageFormat

@Service
class LoginService{

    @Autowired
    RedisService redisService

    @Autowired
    ApplicationContext ac

    void toCache(OnlineUserDomain onlineUserDomain){
        Jedis jedis = redisService.getJedis()
        getKeys(onlineUserDomain).each {it->
            Object res = jedis.eval("""return redis.call($it)""")
        }
    }

    private List getKeys(CacheEntity th){
        List<String> keyList = []
        List<Method> cacheKeys = th.class.methods.findAll { it->
            it.isAnnotationPresent(CacheKey)
        }
        cacheKeys.each {it->
            CacheKey cacheKey = it.getAnnotation(CacheKey)
            String command = cacheKey.command()
            String res = it.invoke(th)
            if (cacheKey.values() == []){
                command = """
                    ${RedisService.addQuote(command)},${RedisService.addQuote(res)},${RedisService.addQuote(RedisService.serialize(th).toString())}
"""            } else{
                List<String> vals = []
                cacheKey.values().each {str->
                    Object obj = ac.getBean(str.split("\\.")[0])
                    String val = obj.getAt(str.split("\\.")[1]) as String
                    vals.add(val)
                }
                Object[] array =new Object[vals.size()]
                vals.toArray(array)
                command = """
                    ${RedisService.addQuote(command)},${RedisService.addQuote(res)},${array.join(",")},${RedisService.addQuote(RedisService.serialize(th).toString())}
"""
            }
            keyList.add(command)
        }
        return keyList
    }
}
