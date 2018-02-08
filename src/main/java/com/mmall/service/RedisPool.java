package com.mmall.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @Author: XBlue
 * @Date: Create in 2018/2/722:02
 * @Description:
 * @Modified By:
 */
//配置成一个让spring管理的类才能获取容器里类，不封装也能获取不过要通过ApplicationContextHelper
    //封装从池中获取单例 类似于数据库连接池 之所以数据库你没写是因为druid已经帮你弄好了
@Service("redisPool")
@Slf4j
public class RedisPool {
    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis instance() {
        return shardedJedisPool.getResource();
    }

    public void safeClose(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            log.error("return redis source exception", e);
        }
    }
}
