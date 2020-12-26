/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Apr 3, 2019
 *******************************************************************************/
package org.gocom.coframe.starter;

import org.gocom.coframe.core.jpa.CoframeJpaRepositoryFactoryBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Configuration
@ComponentScan(basePackages = { "org.gocom.coframe" })
@EntityScan(basePackages = "org.gocom.coframe.model")
@EnableJpaRepositories(repositoryFactoryBeanClass = CoframeJpaRepositoryFactoryBean.class, basePackages = "org.gocom.coframe.repository")
public class CoframeStarterConfig {
	// 参考： https://www.cnblogs.com/jiawenjun/p/8350823.html
	// Spring Boot Application如果依赖这种有jpa扫描的Starter必须添加@EntityScan注解， 否则自己项目中的实体将不会被扫描到,相应repository实例化时也会出错
}
