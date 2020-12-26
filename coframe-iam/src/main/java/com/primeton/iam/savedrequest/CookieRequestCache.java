/*******************************************************************************
 * Copyright (c) 2001-2018 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on 2018年07月10日 17:57:50
 ******************************************************************************/

package com.primeton.iam.savedrequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 该类不适应,应用服务器,如 Tomcat 等可能会对 header 的maxSize 有所限制,如果将一个请求序列化到 cookie, 可能会导致
 * 服务器端500错误
 *
 * @author huzd@primeton.com
 */
public class CookieRequestCache implements RequestCache {
	
	private static Logger log = LoggerFactory.getLogger(CookieRequestCache.class);
    static final String SAVED_REQUEST = "saveRequest";

    private PortResolver portResolver = new PortResolverImpl();
    private RequestMatcher requestMatcher = AnyRequestMatcher.INSTANCE;
    private String cookieName = SAVED_REQUEST;

    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (requestMatcher.matches(request)) {
            DefaultSavedRequest savedRequest = new DefaultSavedRequest(request,
                    portResolver);

            // TODO 序列化SavedRequest
            String savedRequestValue = null;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                ObjectOutput objectOutput = new ObjectOutputStream(out);
                objectOutput.writeObject(savedRequest);
                objectOutput.flush();
                savedRequestValue = Base64.getEncoder().encodeToString(out.toByteArray());
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }
            Cookie cookie = new Cookie(cookieName, savedRequestValue);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60);
            response.addCookie(cookie);
        } else {
            log.debug("Request not saved as configured RequestMatcher did not match");
        }
    }

    public SavedRequest getRequest(HttpServletRequest currentRequest,
                                   HttpServletResponse response) {
        Cookie[] cookies = currentRequest.getCookies();
        if (cookies == null) {
            return null;
        }
        Cookie savedRequestCookie = null;
        for (Cookie cookie : cookies) {
            if (cookieName.equalsIgnoreCase(cookie.getName())) {
                savedRequestCookie = cookie;
                break;
            }
        }

        if (savedRequestCookie != null) {
            String savedRequestValue = savedRequestCookie.getValue();
            ObjectInput input = null;
            try {
                byte[] bytes = Base64.getDecoder().decode(savedRequestValue);
                input = new ObjectInputStream(new ByteArrayInputStream(bytes));
                DefaultSavedRequest savedRequest = (DefaultSavedRequest) input.readObject();
                return savedRequest;
            } catch (IOException | ClassNotFoundException e) {
                log.warn(e.getMessage(), e);
            } finally {
                try {
                    if (input != null) {                     
                        input.close();
                    }
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }

        return null;
    }

    public void removeRequest(HttpServletRequest currentRequest,
                              HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public HttpServletRequest getMatchingRequest(HttpServletRequest request,
                                                 HttpServletResponse response) {
        DefaultSavedRequest saved = (DefaultSavedRequest) getRequest(request, response);

        if (saved == null) {
            return null;
        }

        if (!saved.doesRequestMatch(request, portResolver)) {
            log.debug("saved request doesn't match");
            return null;
        }

        removeRequest(request, response);

        return new SavedRequestAwareWrapper(saved, request);
    }

    /**
     * Allows selective use of saved requests for a subset of requests. By default any
     * request will be cached by the {@code saveRequest} method.
     * <p>
     * If set, only matching requests will be cached.
     *
     * @param requestMatcher a request matching strategy which defines which requests
     *                       should be cached.
     */
    public void setRequestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    public void setPortResolver(PortResolver portResolver) {
        this.portResolver = portResolver;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
