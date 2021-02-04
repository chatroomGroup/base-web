package com.cai.web.service

import com.cai.general.core.BaseService
import com.cai.general.core.Session
import com.cai.general.util.log.ErrorLogManager
import com.cai.general.util.response.Errors
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.web.dao.UserMapper
import com.cai.web.domain.User
import com.cai.web.message.UserMessage
import com.cai.web.message.WebMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService extends BaseService<User>{

    @Autowired
    UserMapper userMapper

    @Autowired
    UserPasswordService upSvc

    @Override
    Errors validateEntity(Session sess, User entity, Errors errors) {
        try{
            if (!userMapper.existsByAccount(entity.account).empty)
                errors.reject(UserMessage.ERROR.USER_ERROR_0001, entity.account)
            def i = 1/0
        }catch(Throwable t){
            ErrorLogManager.logException(sess, t)
            errors.reject(WebMessage.ERROR.MSG_ERROR_0000)
        }
        errors
    }

    @Override
    ResponseMessage afterDelete(Session sess, User obj) {
        redisService.tryOpJedis{op->
            //删除对应缓存信息
            op.del(obj.getCacheKey())
        }
        // 删除所有相关联的userPassword
        upSvc.deleteEntityByUserId(sess, obj.id)
    }

    ResponseMessage destroyUserInfo(Session sess, String account){
        ResponseMessage rsp = ResponseMessageFactory.success()
        try{
            User user = userMapper.getFirstByAccount(account)
            if (user)
                rsp = deleteEntity(sess, user)
            return rsp
        }catch(Throwable t){
            ErrorLogManager.logException(sess, t)
            return ResponseMessageFactory.error(WebMessage.ERROR.MSG_ERROR_0000)
        }

    }

    User getEntity(Session sess, String account){
        return userMapper.getFirstByAccount(account)
    }
}
