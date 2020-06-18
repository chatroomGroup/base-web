package com.cai.web.domain

import com.cai.web.core.CacheEntity

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class OnlineUserDomain extends CacheEntity{

    String user

    AtomicReference<String> token

    @Override
    Object getEntityId() {
        return "$user:$token"
    }

    //身份验证
    static Object getAuthCacheKey(String user, String token) {
        return "AUTH:$user:$token"
    }

    //访问api验证 - 作用于频繁访问场景
    static Object getAccessCacheKey(String user, String token) {
        return "ACCESS:$user:$token"
    }

    //超时验证 - 用户链接超时
    static Object getTimeoutCacheKey(String user, String token){
        return "TIMEOUT:$user:$token"

    }
}
