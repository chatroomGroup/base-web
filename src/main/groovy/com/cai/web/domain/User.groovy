package com.cai.web.domain

import com.cai.general.core.BaseEntity
import com.cai.general.core.EntityType
import com.cai.web.dao.UserRepository
import org.springframework.stereotype.Component

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import java.sql.Timestamp

@Component
@Entity(name = User.TABLE_NAME)
@Table(name = User.TABLE_NAME)
class User extends BaseEntity{

    static DEFINE = define([
            table: TABLE_NAME,
            cache: true,
            type: EntityType.SQL,
            repository: UserRepository.class
    ])

    final static String TABLE_NAME = "g_user";

    @Id
    @GeneratedValue
    Long id

    String account

    String name

    String created

    Integer status

    String userDef1

    String userDef2

    String userDef3

    String userDef4

    String userDef5

    Integer version

    Timestamp lastUpdated

    String lastUpdatedBy

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = UserPassword)
    @JoinColumn(name = "id", referencedColumnName = "userId")
    UserPassword userPassword

    @Override
    Object getEntityId() {
        return id
    }

    @Override
    String getCacheKey() {
        return "$TABLE_NAME:$account"
    }
}