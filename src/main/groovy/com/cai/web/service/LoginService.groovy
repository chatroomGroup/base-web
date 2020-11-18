package com.cai.web.service

import com.cai.general.core.BaseService
import com.cai.general.core.Session
import com.cai.general.util.encode.PasswordUtil
import com.cai.general.util.log.ErrorLogManager
import com.cai.general.util.log.LogParser
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.redis.RedisService
import com.cai.web.core.CacheEntity
import com.cai.web.core.CacheKey
import com.cai.web.core.IgnoreAuthStore
import com.cai.web.dao.UserMapper
import com.cai.web.domain.CallRedisLog
import com.cai.web.domain.OnlineUserDomain
import com.cai.web.domain.Status
import com.cai.web.domain.User
import com.cai.web.domain.UserPassword
import com.cai.web.message.WebMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Service
import redis.clients.jedis.Jedis

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.lang.reflect.Method
import java.text.MessageFormat

import static com.cai.general.util.session.SessionUtils.createSession
import static com.cai.general.util.session.SessionUtils.saveSession

@Service
class LoginService extends BaseService{

    @Autowired
    ApplicationContext ac

    @Autowired
    UserMapper userMapper

    @Autowired
    UserPasswordService upSvc

    void toCache(OnlineUserDomain onlineUserDomain){
        Jedis jedis = redisService.getJedis()
        getKeys(onlineUserDomain).each {it->
            Object res = jedis.eval("""return redis.call($it)""")
            ac.publishEvent(new CallRedisLog(it, res))
        }
        jedis.close()
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


    ResponseMessage login(Session sess, String account, String password){
        UserPassword up
        try{
            if (!account)
                return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0005)
            if (!password)
                return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0010)
            List<User> user = userMapper.getUserByAccount(account)
            if (!user || user.isEmpty())
                return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0006)
            //账号被锁定 相同账号是不被允许的
            if (user.size() > 1) {
                redisLockService.tryOpLock(user[0].getCacheKey(), {
                    userMapper.disableAccountStatusByIds(user.collect { it.id })
                })
                return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0007)
            }
            up = upSvc.getUserPassByAccount(null, user[0].id)
            if (!up)
                return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0006)
            if (!PasswordUtil.deciphering(up.salt, password, up.password))
                return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0009)
            if (user[0].status != Status.UserStatus.OPEN){
                return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0007)
            }

            return ResponseMessageFactory.success("login")
        }catch(Throwable t){
            t.printStackTrace()
            ErrorLogManager.logException(null, t)
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0000)
        }

    }
}
