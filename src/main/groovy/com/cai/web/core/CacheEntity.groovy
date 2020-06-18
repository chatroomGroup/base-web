package com.cai.web.core

import com.cai.general.core.BaseEntity
import com.cai.general.util.response.ResponseMessage
import redis.clients.jedis.Jedis

abstract class CacheEntity extends BaseEntity{
    List<String> cacheKeys = []

    void addKeys(Collection<String> keys){
        cacheKeys.addAll(keys)
    }

}
