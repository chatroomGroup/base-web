package com.cai.web.service

import com.cai.general.core.BaseService
import com.cai.general.core.Session
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.web.dao.UserPasswordRepository
import com.cai.web.dao.UserRepository
import com.cai.web.domain.User
import com.cai.web.message.UserMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService extends BaseService<User>{

    @Autowired
    UserRepository userRepository

    @Autowired
    UserPasswordService upSvc

    @Override
    ResponseMessage afterCreate(Session sess, User obj) {
        if (userRepository.existsByAccount(obj.account))
            return ResponseMessageFactory.error(UserMessage.ERROR.USER_ERROR_0001, obj.account)
        return ResponseMessageFactory.success()
    }

    @Override
    void beforeDelete(Session sess, User obj) {
        // 删除所有相关联的userPassword
        upSvc.deleteEntityByUserId(sess, obj.id)
    }
}
