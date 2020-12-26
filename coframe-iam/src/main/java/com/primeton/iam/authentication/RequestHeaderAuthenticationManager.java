/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月06日 17:28:09
 ******************************************************************************/

package com.primeton.iam.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huzd@primeton.com
 * @see RequestHeaderToken
 * @see RequestHeaderTokenAuthenticationFilter
 */
public class RequestHeaderAuthenticationManager implements AuthenticationManager {


    private final List<RequestHeaderAuthenticationChecker> checkers = new ArrayList<>();

    public RequestHeaderAuthenticationManager(List<RequestHeaderAuthenticationChecker> checkers) {
        this.checkers.addAll(checkers);
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() == null ||
                !(authentication.getPrincipal() instanceof RequestHeaderToken)) {
            return null;
        }
        RequestHeaderToken token = (RequestHeaderToken) authentication.getPrincipal();
        return checkToken(token);
    }

    /**
     * 检查 token 的合法性
     *
     * @param token
     */
    private Authentication checkToken(RequestHeaderToken token) {
        for (RequestHeaderAuthenticationChecker checker : checkers) {
            Authentication authentication = checker.checkToken(token);
            if (authentication != null) {
                return authentication;
            }
        }
        return null;
    }
}
