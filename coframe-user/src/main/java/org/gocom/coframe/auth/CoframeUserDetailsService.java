/**
 * 
 */
package org.gocom.coframe.auth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.auth.config.AuthProperties;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.sdk.cache.CofCacheKey;
import org.gocom.coframe.sdk.cache.ICacheManager;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.gocom.coframe.sdk.model.CofRole;
import org.gocom.coframe.sdk.model.CofUser;
import org.gocom.coframe.sdk.util.JsonUtil;
import org.gocom.coframe.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

/**
 * @author qinscx
 *
 */
@Component
public class CoframeUserDetailsService implements UserDetailsService {
	private Logger log = LoggerFactory.getLogger(CoframeUserDetailsService.class);
	private DateFormat df = new SimpleDateFormat(CoframeConstants.DATE_TIME_FORMAT);

	@Value("${coframe.auth.relogin-keep-token:false}")
	private boolean reloginKeepToken;

	@Autowired
	private ICacheManager cacheManager;

	@Autowired
	private IUserService userService;

	@Autowired
	private CofCacheKey cacheKey;

	@Autowired
	private AuthProperties authProperties;

	public CofUser getLoginedUser(String username) throws AuthenticationException {
		CofUser user = userService.getUserByName(username);
		if (user != null) {
			if (user.getRoles() == null) {
				user.setRoles(toSimpleRoles(userService.getUserRoles(user.getId())));
				cacheManager.put(cacheKey.userName2User(user.getName()), user); // ????????????????????????????????????
			}
			return user;
		}
		return null;
	}

	/**
	 * ??????????????????
	 * 
	 * @param userDetails
	 * @return ???????????????0????????? id?????????1??? token
	 */
	public CofUser saveUserLoginInfo(UserDetails userDetails) {
		CofUser user = userService.getUserByName(userDetails.getUsername());
		if (user != null) {
			String salt = user.getSalt();
			Date loginTime = user.getLastLogin();

			// ??????????????????????????? token
			CofUser tokenUser = new CofUser();
			tokenUser.setId(user.getId());
			tokenUser.setName(user.getName());
			tokenUser.setTenantId(user.getTenantId());

			Set<CofRole> roles = toSimpleRoles(userService.getUserRoles(user.getId()));
			user.setRoles(roles);
			tokenUser.setRoles(roles); // ?????????(?????? id)?????? token

			// ?????????????????????????????? token
			boolean useOldToken = false;
			if (reloginKeepToken) {
				// ?????????????????????
				if (salt != null && loginTime != null) {
					// ?????????????????????????????????
					Calendar time = Calendar.getInstance();
					time.setTime(loginTime);
					if (!time.after(expiresTime(loginTime))) {
						useOldToken = true;
					}
				}
			}

			// ???????????????????????? token??????????????????????????????????????????????????????????????? salt ??? loginTime??????????????? token
			if (!useOldToken) {
				salt = BCrypt.gensalt();
				loginTime = new Date();
				user.setSalt(salt);
				user.setLastLogin(loginTime);
				userService.updateSalt(user.getId(), salt);
			}

			// ??????token
			Algorithm algorithm = Algorithm.HMAC256(salt);
			Date expiresTime = expiresTime(loginTime);
			String token = JWT.create().withSubject(JsonUtil.toJson(tokenUser)).withExpiresAt(expiresTime).withIssuedAt(loginTime).sign(algorithm);
			log.info("JWT Token is generated at {} for user {}, and will be expired at {}", df.format(loginTime), user.getName(), df.format(expiresTime));

			// ?????????????????????
			cacheManager.put(cacheKey.userName2User(user.getName()), user); // userName -> user??????????????????????????????????????????????????????user ??? salt ????????????, ?????? jwt ???????????????

			user.setToken(token);
			return user;
		}
		throw CoframeErrorCode.USER_NOT_FOUND_BY_NAME.runtimeException(userDetails.getUsername());
	}

	@SuppressWarnings("unchecked")
	private Set<CofRole> toSimpleRoles(Set<CofRole> roles) {
		if (roles == null) {
			return Collections.EMPTY_SET;
		}
		return roles.stream().map(x -> new CofRole(x.getId())).collect(Collectors.toSet());
	}

	private Date expiresTime(Date time) {
		Calendar expiresTime = Calendar.getInstance();
		expiresTime.setTime(time);
		expiresTime.add(Calendar.SECOND, authProperties.getTokenExpired());
		return expiresTime.getTime();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CofUser user = userService.getUserByName(username);
		if (user == null) {
			log.error("Can not find user by name: {}", username);
			throw CofErrorCode.USER_NAME_NOT_FOUND.runtimeException();
		}
		if (CoframeConstants.STATUS_DISABLED.equals(user.getStatus())) {
			log.error("User {} is disabled, not allowed to login", username);
			throw CofErrorCode.USER_DISABLED.runtimeException();
		}
		return org.springframework.security.core.userdetails.User//
				.withUsername(username)//
				.password(userService.getUserPassword(user.getId()))//
				.roles("USER").accountExpired(false)//
				.accountLocked(false)//
				.credentialsExpired(false)//
				.disabled(false)//
				.build();
	}

	public void deleteUserLoginInfo(String userName) {
		CofUser user = userService.getUserByName(userName);
		cacheManager.delete(cacheKey.userName2User(userName));
		if (user != null) {
			user.setSalt("");
			userService.updateSalt(user.getId(), "");
		}
	}
}
