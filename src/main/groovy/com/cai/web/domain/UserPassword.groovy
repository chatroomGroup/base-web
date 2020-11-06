package com.cai.web.domain

import com.cai.general.core.BaseEntity
import com.cai.general.core.EntityType
import com.cai.web.dao.UserPasswordRepository
import com.cai.web.dao.UserRepository
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
@Entity(name = UserPassword.TABLE_NAME)
@Table(name = UserPassword.TABLE_NAME)
class UserPassword extends BaseEntity{

    static DEFINE = define([
            table: TABLE_NAME,
            cache: true,
            type: EntityType.SQL,
            repository: UserPasswordRepository.class
    ])

    final static String TABLE_NAME = "g_user_password";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id

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
