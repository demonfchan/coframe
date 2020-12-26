/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 25, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.service;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.cache.CofCacheKey;
import org.gocom.coframe.sdk.cache.ICacheManager;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.gocom.coframe.sdk.model.CofFunction;
import org.gocom.coframe.sdk.model.CofRole;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.sdk.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@Component
public class CofTokenService {
	private Logger log = LoggerFactory.getLogger(CofTokenService.class);
	@Autowired
	private ICacheManager cacheManager;

	@Autowired
	private CofCacheKey cacheKey;

	/**
	 * 只单纯解码 token，取出其中的用户信息
	 * 
	 * @param token
	 * @return
	 */
	public DecodedJWT decode(String token) {
		if (token == null) {
			throw CofErrorCode.TOKEN_EMPTY.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}

		DecodedJWT jwt = null;
		try {
			jwt = JWT.decode(token);
		} catch (JWTDecodeException e1) {
			log.warn("Jwt decode token failed, msg is: {}", e1.getLocalizedMessage());
			throw CofErrorCode.TOKEN_BROKEN.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
		return jwt;
	}

	/**
	 * 从 jwt 中解析出用户信息
	 * 
	 * @param jwt
	 * @return
	 */
	public CofUser analytic(String token) {
		return analytic(decode(token));
	}

	/**
	 * 从 jwt 中解析出用户信息
	 * 
	 * @param jwt
	 * @return
	 */
	public CofUser analytic(DecodedJWT jwt) {
		CofUser user = null;
		try {
			user = JsonUtil.toObject(jwt.getSubject(), CofUser.class);
		} catch (Exception e) {
			log.warn("Jwt subject convert to CofUser failed, msg is: {}", e.getLocalizedMessage());
			throw CofErrorCode.TOKEN_BROKEN.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
		return user;
	}

	/**
	 * 解码 token，并从缓存中取回用户的详细信息
	 * 
	 * @param token
	 * @return
	 */
	public CofUser retrieve(DecodedJWT jwt) {
		CofUser user = null;
		try {
			user = cacheManager.get(cacheKey.userName2User(analytic(jwt).getName()), CofUser.class).get();
		} catch (Exception e) {
			log.warn("Retrieve user from redis cache failed, msg is: {}", e.getLocalizedMessage());
		}
		if (user == null) {
			throw CofErrorCode.USER_NOT_LOGIN.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
		return user;
	}

	/**
	 * 校验 token 是否合法
	 * 
	 * @param token
	 * @return
	 */
	public void validate(String token) {
		validate(decode(token), null);
	}

	/**
	 * 校验 token 是否合法
	 * 
	 * @param token
	 * @param cofUser 从缓存中可以取得
	 * @return
	 */
	public void validate(DecodedJWT jwt, CofUser cofUser) {
		// 是否超时
		if (Calendar.getInstance().getTime().after(jwt.getExpiresAt())) {
			throw CofErrorCode.TOKEN_EXPIRED.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
		// 取用户
		if (cofUser == null) {
			cofUser = retrieve(jwt);
		}
		if (cofUser == null) {
			throw CofErrorCode.USER_NOT_LOGIN.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
		// 用户中不含 salt
		if (cofUser.getSalt() == null) {
			throw CofErrorCode.USER_NOT_LOGIN.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
		// 校验用户状态, 只有为 enabled 的用户才允许登陆
		if (!CofConstants.STATUS_ENABLED.equals(cofUser.getStatus())) {
			throw CofErrorCode.USER_DISABLED.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
		// 校验token是否合法
		String encryptSalt = cofUser.getSalt();
		try {
			Algorithm algorithm = Algorithm.HMAC256(encryptSalt);
			JWTVerifier verifier = JWT.require(algorithm).withSubject(jwt.getSubject()).build();
			verifier.verify(jwt.getToken());
		} catch (Exception e) {
			log.warn("Jwt verifier token failed, msg is: {}", e.getLocalizedMessage());
			throw CofErrorCode.TOKEN_INVALID.responseCode(HttpStatus.UNAUTHORIZED.value()).runtimeException();
		}
	}

	/**
	 * 取用户的所有功能
	 * 
	 * @param user
	 * @return
	 */
	public Set<CofFunction> retrieveFunctions(CofUser user) {
		Set<CofFunction> functions = new HashSet<>();
		if (user.getRoles() != null) {
			user.getRoles().forEach(r -> {
				CofRole role = cacheManager.get(cacheKey.roleId2Role(r.getId()), CofRole.class).get();
				if (role != null && role.getFunctions() != null) {
					functions.addAll(role.getFunctions());
				}
			});
		}
		return functions;
	}
}
