package com.cai.web.domain

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.cai.general.core.BaseEntity
import com.cai.general.core.EntityType
import com.cai.web.dao.UserPasswordMapper
import org.springframework.stereotype.Component

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Component
@TableName(UserPassword.TABLE_NAME)
class UserPassword extends BaseEntity{

    static DEFINE = define([
            table: TABLE_NAME,
            cache: true,
            type: EntityType.SQL,
            mapper: UserPasswordMapper.class
    ])

    final static String TABLE_NAME = "g_user_password";

    long id

    String password

    String salt

    String created

    String createdBy

    String lastUpdatedBy

    Integer version

    String lastUpdated

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = User)
//    @JoinColumn(name = "userId", referencedColumnName = "id")
//    User user

    long userId

    @Override
    Object getEntityId() {
        return id
    }

    @Override
    String getCacheKey() {
        return "$TABLE_NAME:$id"
    }
}
