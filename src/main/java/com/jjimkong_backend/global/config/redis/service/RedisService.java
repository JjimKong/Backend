package com.jjimkong_backend.global.config.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;
    private Jackson2HashMapper mapper = new Jackson2HashMapper(true);

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    public void writeHash(String key, Object object) {
        Map<String, Object> mappedHash = mapper.toHash(object);
        hashOperations.putAll(key, mappedHash);
    }

    public Object loadHash(String key) {
        Map<String, Object> loadedHash = hashOperations.entries(key);
        return mapper.fromHash(loadedHash);
    }

    public void setListValues(String key, Object value){
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(key, value);
    }

    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return null;
        }
        return (String) values.get(key);
    }

    public List<Long> getLongListOpsValues(String key){
        ListOperations<String, Long> listOperations = redisTemplate.opsForList();
        return listOperations.range(key, 0, -1);
    }

    public List<String> getStringListOpsValues(String key){
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations.range(key, 0, -1);
    }

    public void deleteValues(String key) {
        if(Boolean.FALSE.equals(redisTemplate.delete(key))){
            //throw new RedisException(ErrorCode.REDIS_DATA_DELETE_ERROR);
            log.error("Redis 데이터를 삭제하지 못했습니다.");
        }
    }

    public boolean checkHasKey(String key){
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

}
