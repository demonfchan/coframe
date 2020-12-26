/**
 * 
 */
package org.gocom.coframe.auth.up;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.CofContext;
import org.gocom.coframe.sdk.model.CofLoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用户名与密码校验过滤器
 * 
 * @author qinscx
 *
 */
public class UPFilter extends AbstractAuthenticationProcessingFilter {
	private static Logger logger = LoggerFactory.getLogger(UPFilter.class);

	public UPFilter() {
		super(new AntPathRequestMatcher(CofConstants.COF_LOGIN_URI, HttpMethod.POST.name()));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
		String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
		String username = null, password = null, tenantId = null;
		if (StringUtils.hasText(body)) {
			ObjectMapper objectMapper = new ObjectMapper();
			CofLoginUser user = new CofLoginUser();
			try {
				user = objectMapper.readValue(body, CofLoginUser.class);
				username = user.getUsername();
				password = user.getPassword();
				tenantId = user.getTenantId();
			} catch (Exception e) {
				logger.error("Convert json to user failed while doing login authentication. msg is: " + e.getLocalizedMessage());
			}
		}
		if (username == null)
			username = "";
		if (password == null)
			password = "";
		if (tenantId == null) {
			CofContext.getContext().remove(CoframeConstants.CONTEXT_TENANT_ID);
		} else {
			CofContext.getContext().put(CoframeConstants.CONTEXT_TENANT_ID, tenantId); // 将 tenatId 放入 context 中。这样后续查询的时候，会自动带上这一条件
		}
		username = username.trim();
		// 封装到token中提交
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
