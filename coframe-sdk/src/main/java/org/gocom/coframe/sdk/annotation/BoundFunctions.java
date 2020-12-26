/*
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 5/31/19 3:28 PM
 */

package org.gocom.coframe.sdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识方法绑定的功能(方法对应多个功能). 用户配置了这些功能中的任意一个, 方法就允许调用
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BoundFunctions {
	/**
	 * 方法允许调用需要的功能. 只要用户所拥有的功能中能有任意一个匹配, 此方法就允许用户进行调用
	 * 
	 * @return
	 */
	BoundFunction[] functions() default {};

	/**
	 * 方法名
	 * 
	 * @return
	 */
	String method() default "";
}
