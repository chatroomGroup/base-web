package com.cai.web.domain

class Status {

    // 用户账号状态
    static final class UserStatus {
        static final Long LOCKED = 0 // 锁定
        static final Long OPEN = 1 // 开放
        static final Long UNLOCKING = 2 // 解锁中
    }
}
