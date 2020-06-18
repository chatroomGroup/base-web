package com.cai.web.core

import com.cai.general.util.response.ResponseMessage
import redis.clients.jedis.Jedis

interface CacheEntityAction {

    abstract def <T extends CacheEntity> ResponseMessage cache(Jedis jedis, T entity)

}
