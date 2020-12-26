/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jul 31, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * 标识方法绑定的功能(单个功能). 用户只有为他配置了此功能, 方法才允许调用
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BoundFunction {
	/**
	 * 功能编码
	 * 
	 * @return
	 */
	@AliasFor("code")
	String value() default "";

	/**
	 * 功能编码
	 * 
	 * @return
	 */
	@AliasFor("value")
	String code() default "";

	/**
	 * 功能名称, 可选, 如果需要通过代码扫描以导入应用中定义的功能, 则需要配置. 扫描导入时, 如果没有配置, 默认设置为功能编码
	 * 
	 * @return
	 */
	String name() default "";
}
