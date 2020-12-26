/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 25, 2019
 *******************************************************************************/
package org.gocom.coframe.core.cache;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.gocom.coframe.sdk.cache.ICacheManager;
import org.gocom.coframe.sdk.util.JsonUtil;

/**
 * 使用内存做为缓存存储
 * @author qinsc (mailto:qinsc@primeton.com)
 */
public class MemoryCache implements ICacheManager {
    private final static ConcurrentMap<String, String> store = new ConcurrentHashMap<>(256);

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        if (store.containsKey(key)) {
            return Optional.ofNullable(JsonUtil.toObject(store.get(key), clazz));
        }
        return Optional.empty();
    }

    @Override
    public void put(String key, Object obj) {
        store.put(key, JsonUtil.toJson(obj));
    }

    @Override
    public void delete(String... keys) {
        if (keys != null) {
            for (String key: keys){
                store.remove(key);
            }
        }
    }

    @Override
    public void deleteWithPattern(String keyPattern) {
        Set<String> keys = new HashSet<>();
        store.keySet().forEach(key -> {
            if (key.matches(keyPattern)){
                keys.add(key);
            }
        });
        if (keys.size() > 0) {
            keys.forEach(key -> {
                store.remove(key);
            });
        }
    }
}
