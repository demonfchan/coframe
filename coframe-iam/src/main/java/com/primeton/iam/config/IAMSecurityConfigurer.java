/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月06日 18:01:27
 ******************************************************************************/

package com.primeton.iam.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 对默认的 安全过滤器连进行配置
 *
 * @author huzd@primeton.com
 */
public interface IAMSecurityConfigurer {

    void configure(HttpSecurity http) throws Exception;

}
