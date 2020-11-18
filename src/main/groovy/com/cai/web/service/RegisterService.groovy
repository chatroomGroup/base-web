package com.cai.web.service

import com.cai.general.core.BaseService
import com.cai.general.core.Session
import com.cai.general.util.encode.EncryptionEntity
import com.cai.general.util.encode.PasswordUtil
import com.cai.general.util.log.ErrorLogManager
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.jdbc.mysql.JdbcTemplate
import com.cai.redis.RedisLockService
import com.cai.web.domain.User
import com.cai.web.domain.UserPassword
import com.cai.web.message.UserMessage
import com.cai.web.message.WebMessage
import org.redisson.api.RLock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import static com.cai.general.util.session.SessionUtils.*
import javax.annotation.PostConstruct

@Service
class RegisterService extends BaseService{

    @PostConstruct
    void init(){
        session = createSession("api", "", app.name)
    }

    Session session

    @Autowired
    UserService userService

    @Autowired
    UserPasswordService upSvc

    @Autowired
    RedisLockService redisLockService

    /**
     * 注册用户
     * @param account
     * @param name
     * @param password
     * @return
     */
    ResponseMessage register(String account, String name, String password){
        User newUser = new User()
        UserPassword newPassword = new UserPassword()
        ResponseMessage rsp
        RLock rLock
        rLock = redisLockService.getAndTryLock(getCacheKey("register", account))
        if (!rLock)
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0008)
        try{
            jdbcTemplate.beginTransaction()
            //  创建user
            newUser.name = name
            newUser.account = account
            rsp = userService.createEntity(session, newUser)
            if (!rsp.isSuccess){
                jdbcTemplate.rollback()
                return rsp
            }
            newUser = userService.getEntity(session, newUser.account)
            if (!newUser)
                return ResponseMessageFactory.error(UserMessage.ERROR.USER_ERROR_0002)

            // 创建userPassword
            EncryptionEntity entity = PasswordUtil.encrypt(password)
            newPassword.password = entity.os
            newPassword.salt = entity.salt
            newPassword.userId = newUser.id
            rsp = upSvc.createEntity(session, newPassword)
            if (!rsp.isSuccess){
                jdbcTemplate.rollback()
                return rsp
            }
            jdbcTemplate.commit()
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            t.printStackTrace()
            ErrorLogManager.logException(session, t)
            jdbcTemplate.rollback() // 回滚
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0000)
        }finally{
            if (rLock)
                rLock.unlock() // 解锁
        }

    }


    static String getCacheKey(String prefix, String account){
        return "$prefix:$account"
    }
}
