package com.cai.web.domain

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity(name = UserPassword.TABLE_NAME)
@Table(name = UserPassword.TABLE_NAME)
class UserPassword {

    final static String TABLE_NAME = "g_user_password";

    @Id
    @GeneratedValue
    Long id

    String password

    String salt

    String created

    String createdBy

    String lastUpdatedBy

    Integer version

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = User)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    User user
}
