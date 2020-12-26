/**
 * 
 */
package org.gocom.coframe.auth.jwt;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gocom.coframe.auth.AuthToken;
import org.gocom.coframe.auth.CoframeUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author qinscx
 *
 */
public class JWTSuccessHandler implements AuthenticationSuccessHandler {
//	private Logger log = LoggerFactory.getLogger(JWTSuccessHandler.class);
//	private DateFormat df = new SimpleDateFormat(CoframeConstants.DATE_TIME_FORMAT);
//	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern(CoframeConstants.DATE_TIME_FORMAT);
	private int tokenRefreshInterval = 1200; // 刷新时间默认20分钟
	private CoframeUserDetailsService userDetailsService;

	public JWTSuccessHandler(CoframeUserDetailsService userDetailsService, int tokenRefreshInterval) {
		this.tokenRefreshInterval = tokenRefreshInterval;
		this.userDetailsService = userDetailsService;
	}

	protected boolean shouldTokenRefresh(Date expireAt) {
		LocalDateTime expireTime = LocalDateTime.ofInstant(expireAt.toInstant(), ZoneId.systemDefault());
		LocalDateTime freshTime = expireTime.minusSeconds(tokenRefreshInterval);
//		log.info("Check token refresh, token will expire at {}, need refresh after {}", expireTime.format(dtf), freshTime.format(dtf));
		return LocalDateTime.now().isAfter(freshTime);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		AuthToken authToken = (AuthToken) authentication;
		DecodedJWT jwt = JWT.decode(authToken.getToken());
		boolean shouldRefresh = shouldTokenRefresh(jwt.getExpiresAt());
//		log.info("Check token refresh, token genareted at {}, refresh interval is {}, need fresh: {}", df.format(jwt.getIssuedAt()), tokenRefreshInterval, shouldRefresh);
		if (shouldRefresh) {
			String newToken = userDetailsService.saveUserLoginInfo((UserDetails) authentication.getPrincipal()).getToken();
			response.setHeader("Authorization", newToken);
		}
	}
}
