package com.cai.web

import com.cai.BaseWebApplication
import com.cai.general.core.App
import com.cai.general.core.Session
import com.cai.general.util.encode.EncryptionEntity
import com.cai.general.util.encode.PasswordUtil
import com.cai.general.util.session.SessionUtils
import com.cai.web.dao.UserRepository
import com.cai.web.domain.User
import com.cai.web.domain.UserPassword
import com.cai.web.service.UserPasswordService
import com.cai.web.service.UserService
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner)
@SpringBootTest(classes = BaseWebApplication)
class BaseWebApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    UserService usSvc

    @Autowired
    App app

    @Autowired
    UserRepository userRepository

    @Autowired
    UserPasswordService upSvc

    Session sess

    @Before
    void before(){
        sess = SessionUtils.createSession("test","test",app.name)
    }
    @Test
    void saveUser(){
        User user = new User()
        user.name = "test"
        user.account = "test"
        Session sess = SessionUtils.createSession("test","test",app.name)
        println usSvc.createEntity(SessionUtils.createSession("test","test",app.name), user).message

        EncryptionEntity ee = PasswordUtil.encrypt("123")
        UserPassword userPassword = new UserPassword()
        userPassword.salt = ee.salt
        userPassword.password = ee.os
        userPassword.userId = userRepository.getDistinctByAccount(user.account)?.id

        upSvc.createEntity(sess, userPassword)
        void
    }


    @Test
    void delete(){
        User user = userRepository.getDistinctByAccount("test")
        usSvc.deleteEntity(sess, user)
    }

    @Test
    void getEntity(){
        User user = new User()
        user.id = 1L
        user = usSvc.getEntity(sess, user)
        user = usSvc.getEntity(sess, user)
        println user
    }
}
