package com.kaige.service;

import com.kaige.entity.PageResult;
import com.kaige.model.vo.BlogInfo;

import java.util.Map;

public interface RedisService {
    PageResult<BlogInfo> getBlogInfoPageResultByHash(String redisKey, Integer pageNum);

    Object getValueByHashKey(String redisKey, Object id);

    <T>Map<String, T> getMapByValue(String redisKey);

    <T>void saveMapToValue(String redisKey, Map<String, T> map);

    <T> T getObjectByValue(String key, Class t);


    void incrementByKey(String redisKey, int i);

    void expire(String redisKey, long seconds);

    void incrementByHashKey(String blogViewsMap, Long blogId, int i);

    void saveKVToHash(String redisKey, Integer pageNum, PageResult<BlogInfo> pageResult);

    void deleteByKey(String redisKey);
}
