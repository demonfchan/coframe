/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月18日 11:55:08
 ******************************************************************************/

package com.primeton.iam.authentication;

import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;

/**
 * 检查http header认证
 *
 * @author huzd@primeton.com
 */
public interface RequestHeaderAuthenticationChecker {

    /**
     * @return 需要进行检测的HTTP 头
     */
    @NotNull
    String getHeaderName();

    /**
     * 检查认证头,
     *
     * @param token 请求头
     * @return 如果能够处理对应的请求头, 则返回处理后的认证对象,
     * 如果不能够处理则返回 null
     */
    Authentication checkToken(RequestHeaderToken token);

}
