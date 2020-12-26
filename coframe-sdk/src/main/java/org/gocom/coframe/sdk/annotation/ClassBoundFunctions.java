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
 * 有些方法的实现，是定义在父类中，无法修改父类, 不好为这些方法绑定功能. 此注解就是为了解决这个问题, 它可以配置在在子类上,
 * 为父类中的方法定义绑定的功能, 以实现方法调用权限的控制
 * 
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassBoundFunctions {
	/**
	 * 允许方法调用的功能定义
	 * 
	 * @return
	 */
	BoundFunctions[] value() default {};
}
