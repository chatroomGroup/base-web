package com.cai.web.service

import com.cai.general.core.BaseService
import com.cai.general.core.Session
import com.cai.general.util.log.ErrorLogManager
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.web.dao.UserPasswordRepository
import com.cai.web.domain.UserPassword
import com.cai.web.message.UserPasswordMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserPasswordService extends BaseService<UserPassword>{

    @Autowired
    UserPasswordRepository userPasswordRepository

    @Override
    ResponseMessage afterCreate(Session sess, UserPassword obj) {
        long res = userPasswordRepository.countByUserId(obj.userId)
        if (res > 0)
            return ResponseMessageFactory.error(UserPasswordMessage.ERROR.USER_PASSWORD_ERROR_0001)
        return ResponseMessageFactory.success()
    }


    ResponseMessage deleteEntityByUserId(Session sess, long userId){
        try{
            userPasswordRepository.deleteAllByUserId(userId)
            return ResponseMessageFactory.success()
        }catch(Throwable t){
            t.printStackTrace()
            ErrorLogManager.logException(sess, t)
        }
    }
}
