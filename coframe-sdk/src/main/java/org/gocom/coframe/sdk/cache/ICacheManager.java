/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 3:27 PM
 */
package org.gocom.coframe.sdk.cache;

import java.util.Optional;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 */
public interface ICacheManager {
    /**
     * 获取指定 key 与指定类型的对象
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Optional<T> get(String key, Class<T> clazz);

    /**
     * 保存对象至缓存
     *
     * @param key
     * @param obj
     */
    public void put(String key, Object obj);

    /**
     * 从缓存中删除
     *
     * @param keys
     */
    public void delete(String... keys);

    /**
     * 从缓存中删除
     *
     * @param keyPattern
     */
    public void deleteWithPattern(String keyPattern);
}
