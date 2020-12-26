package org.gocom.coframe.core.support;

import java.util.UUID;

/**
 * @author wangwb
 *
 */
public class CoframeIdUtil {
	/**
	 * 生成一个 Id
	 * @return
	 */
	public static String generateId(Class<?> clazz) {
		return UUID.randomUUID().toString();
	}
}
