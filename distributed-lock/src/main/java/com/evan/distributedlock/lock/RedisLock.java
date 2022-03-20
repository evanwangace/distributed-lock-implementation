package com.evan.distributedlock.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * 通过【Redis】实现的分布式锁，仅供演示参考
 */
@Slf4j
public class RedisLock implements AutoCloseable {

    private final RedisTemplate redisTemplate;
    private final String key;
    private final String value;
    /**
     * 单位：秒
     */
    private final int expireTime;

    public RedisLock(RedisTemplate redisTemplate, String key, int expireTime) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.expireTime = expireTime;
        this.value = UUID.randomUUID().toString();
    }

    /**
     * 获取分布式锁
     */
    public Boolean getLock() {
        RedisCallback<Boolean> redisCallback = connection -> {
            //设置NX
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            //设置过期时间
            Expiration expiration = Expiration.seconds(expireTime);
            //序列化key
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            //序列化value
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
            //执行setnx操作
            return connection.set(redisKey, redisValue, expiration, setOption);
        };

        //获取分布式锁
        return (Boolean) redisTemplate.execute(redisCallback);
    }

    /**
     * 解除分布式锁
     * Lua脚本含义说明：
     * 假设key为redisKey，即get获取key为redisKey的value值，等于传入的value值的话，删除这个redisKey的内容。
     * 在这里即获取redisKey对应的uuid，如果获取的uuid值与传入的uuid相等的话，就删除redisKey对应的数据，达到释放锁的目的。
     */
    public Boolean unLock() {
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        RedisScript<Boolean> redisScript = RedisScript.of(script, Boolean.class);
        List<String> keys = Collections.singletonList(key);

        Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, value);
        log.info("释放锁的结果：{}", result);
        return result;
    }

    /**
     * 关闭资源
     */
    @Override
    public void close() {
        unLock();
    }
}
