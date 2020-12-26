/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 4:59 PM
 */

package org.gocom.coframe.sdk;

import java.util.HashMap;
import java.util.Map;

public class CofContext {
    private static final ThreadLocal<CofContext> CONTEXT = new ThreadLocal<>();

    private Map<String, Object> meta = new HashMap<>();

    public static CofContext getContext() {
        CofContext context = CONTEXT.get();
        if (context == null) {
            context = new CofContext();
            CONTEXT.set(context);
        }
        return context;
    }

    public Object get(String key) {
        return meta.get(key);
    }
    
    @SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> clazz) {
        Object obj = meta.get(key);
        if (obj != null) {
        	return (T)obj;
        }
        return null;
    }

    public void put(String key, Object value) {
        meta.put(key, value);
    }
    
    public void remove(String key) {
        meta.remove(key);
    }
    
    public void clear() {
    	meta.clear();
    }
}


