package com.kaige.service.impl;

import com.kaige.entity.PageResult;
import com.kaige.model.vo.BlogInfo;
import com.kaige.service.RedisService;
import com.kaige.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    RedisTemplate jsonRedisTemplate;
    @Override
    public PageResult<BlogInfo> getBlogInfoPageResultByHash(String redisKey, Integer pageNum) {
        if(jsonRedisTemplate.opsForHash().hasKey(redisKey,pageNum)){
            Object redisResult = jsonRedisTemplate.opsForHash().get(redisKey, pageNum);
            PageResult pageResult = JacksonUtils.convertValue(redisResult, PageResult.class);
            return pageResult;
        }else {
            return null;
        }
    }

    @Override
    public Object getValueByHashKey(String redisKey, Object id) {
        return jsonRedisTemplate.opsForHash().get(redisKey, id);
    }

    @Override
    public <T> Map<String, T> getMapByValue(String redisKey) {
        Map<String,T> redisResult = (Map<String, T>) jsonRedisTemplate.opsForValue().get(redisKey);
        return redisResult;
    }

    @Override
    public <T> void saveMapToValue(String redisKey, Map<String, T> map) {
        jsonRedisTemplate.opsForValue().set(redisKey,map);
    }

    @Override
    public <T> T getObjectByValue(String key, Class t) {
        Object redisResult = jsonRedisTemplate.opsForValue().get(key);
        T object = (T) JacksonUtils.convertValue(redisResult, t);
        return object;
    }

    @Override
    public void incrementByKey(String redisKey, int i) {
        if(i < 0){
            throw new RuntimeException("递增因子必须大于0");
        }
        jsonRedisTemplate.opsForValue().increment(redisKey,i);
    }

    @Override
    public void expire(String redisKey, long seconds) {
        jsonRedisTemplate.expire(redisKey,seconds, TimeUnit.SECONDS);

    }

    @Override
    public void incrementByHashKey(String blogViewsMap, Long blogId, int i) {
        if (i < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        jsonRedisTemplate.opsForHash().increment(blogViewsMap, blogId, i);
    }

    @Override
    public void saveKVToHash(String redisKey, Integer pageNum, PageResult<BlogInfo> pageResult) {
        jsonRedisTemplate.opsForHash().put(redisKey,pageNum,pageResult);
    }

    @Override
    public void deleteByKey(String redisKey) {
        jsonRedisTemplate.delete(redisKey);

    }


}
