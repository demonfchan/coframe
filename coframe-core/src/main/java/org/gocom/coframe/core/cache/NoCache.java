/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Mar 25, 2019
 *******************************************************************************/
package org.gocom.coframe.core.cache;

import java.util.Optional;

import org.gocom.coframe.sdk.cache.ICacheManager;

/**
 * 缓存的默认实现，不缓存数据
 * @author qinsc (mailto:qinsc@primeton.com)
 */
public class NoCache implements ICacheManager {
    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        return Optional.empty();
    }

    @Override
    public void put(String key, Object obj) {
    }

    @Override
    public void delete(String... keys) {
    }

    @Override
    public void deleteWithPattern(String keyPattern) {

    }
}