package com.han.user.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        RedisUtil.redisTemplate = redisTemplate;
    }

    public static boolean setStrValue(String key, String value){
        return setStrValue(key, value, null);
    }

    public static boolean setStrValue(String key, String value, Integer expire){
        if(redisTemplate.hasKey(key)){
            redisTemplate.delete(key);
        }
        redisTemplate.opsForValue().set(key, value, null == expire ? 30 : expire, TimeUnit.MINUTES);
        return redisTemplate.hasKey(key);
    }

    public static String getValue(String key){
        return redisTemplate.hasKey(key) ? (String) redisTemplate.opsForValue().get(key) : null;
    }
}
