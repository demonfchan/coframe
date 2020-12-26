/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 3:27 PM
 */
package org.gocom.coframe.sdk.cache;

import java.util.Optional;
import java.util.Set;

import org.gocom.coframe.sdk.util.JsonUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 使用 redis 做为缓存存储
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
public class RedisCache implements ICacheManager {
	private StringRedisTemplate redisTemplate;

	public RedisCache(StringRedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public <T> Optional<T> get(String key, Class<T> clazz) {
		String value = redisTemplate.opsForValue().get(key);
		if (value == null) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(JsonUtil.toObject(value, clazz));
		}
	}

	@Override
	public void put(String key, Object obj) {
		redisTemplate.opsForValue().set(key, JsonUtil.toJson(obj));
	}

	@Override
	public void delete(String... keys) {
		if (keys != null) {
			for (String key : keys) {
				redisTemplate.delete(key);
			}
		}
	}

	@Override
	public void deleteWithPattern(String keyPattern) {
		Set<String> keys = redisTemplate.keys(keyPattern);
		if (keys != null) {
			redisTemplate.delete(keys);
		}
	}
}
