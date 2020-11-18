package com.cai.web.domain

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import com.cai.general.core.BaseEntity
import com.cai.general.core.EntityType
import com.cai.web.dao.UserMapper
import org.apache.ibatis.annotations.Mapper
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

    @TableField("account")
    String account;

    @TableField("name")
    String name;

    @TableField("created")
    String created;

    @TableField("status")
    Integer status;

    @TableField("userDef1")
    String userDef1;

    @TableField("userDef2")
    String userDef2;

    @TableField("userDef3")
    String userDef3;

    @TableField("userDef4")
    String userDef4;

    @TableField("userDef5")
    String userDef5;

    @TableField("version")
    Integer version;

    @TableField("lastUpdated")
    Timestamp lastUpdated;

    @TableField("lastUpdatedBy")
    String lastUpdatedBy;

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