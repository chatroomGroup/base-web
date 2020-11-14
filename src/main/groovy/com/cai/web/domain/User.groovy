package com.cai.web.domain

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.cai.general.core.BaseEntity
import com.cai.general.core.EntityType
import com.cai.web.dao.UserMapper
import org.springframework.stereotype.Component
import java.sql.Timestamp

@Component
@TableName(User.TABLE_NAME)
class User extends BaseEntity{

    static DEFINE = define([
            table: TABLE_NAME,
            cache: true,
            type: EntityType.SQL,
            mapper: UserMapper.class
    ])

    final static String TABLE_NAME = "g_user";

    @TableId
    long id

    String account

    String name

    String created

    Integer status = Status.UserStatus.OPEN

    String userDef1

    String userDef2

    String userDef3

    String userDef4

    String userDef5

    Integer version

    Timestamp lastUpdated

    String lastUpdatedBy

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = UserPassword)
//    @JoinColumn(name = "id", referencedColumnName = "userId")
//    UserPassword userPassword

    @Override
    Object getEntityId() {
        return id
    }

    @Override
    String getCacheKey() {
        return "$TABLE_NAME:$account"
    }
}