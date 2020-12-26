package com.primeton.iam.client;

import static org.gocom.coframe.sdk.CofConstants.SSO_TOKEN;
import static org.gocom.coframe.sdk.util.CofBeanUtils.copyNotNullProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.gocom.coframe.model.Function;
import org.gocom.coframe.model.Menu;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.User;
import org.gocom.coframe.repository.UserRepository;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.gocom.coframe.sdk.model.CofFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SsoUserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private SSoTokenService iamTokenService;
	
	public Set<Menu> listUserMenus(HttpServletRequest request) {
		return repository.findUserMenus(getSSoUser(request).getId());
	}
	

	public Set<Function> listUserFunctions(HttpServletRequest request) {
		return repository.findUserFunctions(getSSoUser(request).getId());
	}
	
	public Set<String> getUserFuctionCodes(HttpServletRequest request) {
		return getUserFuctions(getSSoUser(request).getId()).stream().map(x -> x.getCode()).collect(Collectors.toSet());
	}
	
	public Set<CofFunction> getUserFuctions(HttpServletRequest request) {
		Set<CofFunction> cofFunctions = new HashSet<>();
		Set<Function> functions = listUserFunctions(getSSoUser(request).getId());
		if (functions != null) {
			cofFunctions = functions.stream().map(x -> copyNotNullProperties(x, new CofFunction())).collect(Collectors.toSet());
		}
		return cofFunctions;
	}
	
	public Set<Role> listRoles(HttpServletRequest request) {
		return repository.listRoles(getSSoUser(request).getId());
	}

	public User getSSoUser(HttpServletRequest request) {
		String token = null;
		if(request.getCookies() !=null) {
			token =  Arrays.asList(request.getCookies()).stream().filter(cookie -> cookie.getName().equals(SSO_TOKEN)).
					map(cookie -> cookie.getValue()).collect(Collectors.joining(""));
		}
		if(token == null || token.equals("")) {
			throw CofErrorCode.TOKEN_EMPTY.runtimeException();
		}
		token = token.split("\\|")[1];
		User user = findByUserName(iamTokenService.analytic(token).getName());
		return user;
	}
	
	/**
	 * 根据用户名找到用户
	 * 
	 * @param userName
	 * @return
	 */
	public User findByUserName(String userName) {
		User.Criteria criteria = new User.Criteria();
		criteria.setName(userName);
		criteria.setUsingLikeQuery(false);
		Optional<User> user = repository.findOne(repository.toSpecification(criteria));
		return user.isPresent() ? user.get() : null;
	}
	
	/**
	 * 获取用户的菜单
	 *
	 * @param userId
	 * @return
	 */
	public Set<Menu> listUserMenus(String userId) {
		return repository.findUserMenus(userId);
	}

	/**
	 * 获取用户的所有可用的功能
	 *
	 * @param userId
	 * @return
	 */
	public Set<Function> listUserFunctions(String userId) {
		return repository.findUserFunctions(userId);
	}

	/**
	 * 获取用户所有可用的功能编码
	 */
	public Set<String> getUserFuctionCodes(String userId) {
		return getUserFuctions(userId).stream().map(x -> x.getCode()).collect(Collectors.toSet());
	}

	public Set<CofFunction> getUserFuctions(String userId) {
		Set<CofFunction> cofFunctions = new HashSet<>();
		Set<Function> functions = listUserFunctions(userId);
		if (functions != null) {
			cofFunctions = functions.stream().map(x -> copyNotNullProperties(x, new CofFunction())).collect(Collectors.toSet());
		}
		return cofFunctions;
	}
}
