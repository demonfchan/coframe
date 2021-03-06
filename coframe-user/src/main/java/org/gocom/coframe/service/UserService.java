/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Feb 28, 2019
 *******************************************************************************/
package org.gocom.coframe.service;

import static org.gocom.coframe.sdk.util.CofBeanUtils.copyNotNullProperties;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.exception.CoframeErrorCode;
import org.gocom.coframe.core.jpa.CoframeJpaRepository;
import org.gocom.coframe.core.service.AbstractPersistentModelService;
import org.gocom.coframe.model.Employee;
import org.gocom.coframe.model.Function;
import org.gocom.coframe.model.Menu;
import org.gocom.coframe.model.PartyAuth;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.User;
import org.gocom.coframe.model.UserPassword;
import org.gocom.coframe.repository.PartyAuthRepository;
import org.gocom.coframe.repository.RoleRepository;
import org.gocom.coframe.repository.UserRepository;
import org.gocom.coframe.sdk.CofContext;
import org.gocom.coframe.sdk.cache.CofCacheKey;
import org.gocom.coframe.sdk.cache.ICacheManager;
import org.gocom.coframe.sdk.exception.CofErrorCode;
import org.gocom.coframe.sdk.model.CofFunction;
import org.gocom.coframe.sdk.model.CofRole;
import org.gocom.coframe.sdk.model.CofUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Service
public class UserService extends AbstractPersistentModelService<User> implements IUserService {
	// ?????? userId -> user password
	private final static ConcurrentMap<String, String> passwords = new ConcurrentHashMap<>(256);

	@Autowired
	private UserRepository repository;

	@Autowired
	private PartyAuthRepository partyAuthRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private CofCacheKey cacheKey;

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	protected CoframeJpaRepository<User, String> getRepository() {
		return repository;
	}

	@Autowired
	private ICacheManager cacheManager;

	@Override
	public void preCreate(User user) {
		// ???????????????????????????
		if (findByUserName(user.getName()) != null) {
			throw CoframeErrorCode.USER_NAME_DUPLICATED.runtimeException();
		}
		// ??????????????????, ???????????????????????????????????????????????????
		if (user.getEmployee() != null && findByEmployeeId(user.getEmployee().getId()) != null) {
			throw CoframeErrorCode.EMPLOYEE_ALREADY_BIND_USER.runtimeException();
		}
		// user.setStatus(CoframeConstants.STATUS_ENABLED);
		// ???????????????????????????????????????
		if (user.getPassword() == null) {
			user.setPassword(encoder.encode(CoframeConstants.DEFAULT_USER_PASSWORD));
		} else {
			user.setPassword(encoder.encode(user.getPassword()));
		}
	}

	@Override
	public void preDelete(String id) {
		// ??????????????????????????????
		PartyAuth.Criteria criteria = new PartyAuth.Criteria();
		criteria.setPartyType(CoframeConstants.PARTY_TYPE_USER);
		criteria.setPartyId(id);
		partyAuthRepository.delete(partyAuthRepository.toSpecification(criteria));
		// ??????????????????
		passwords.remove(id);
	}

	/**
	 * ??????????????????
	 *
	 * @param user
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void changePassword(String userId, UserPassword user) {
		repository.findById(userId).ifPresent(usr -> {
			if (!encoder.matches(user.getOldPassword(), usr.getPassword())) {
				throw CoframeErrorCode.USER_OLD_PASSWORD_WRONG.runtimeException();
			}
			usr.setPassword(encoder.encode(user.getNewPassword()));
			usr.setSalt(null); // ???????????????token ????????????
			repository.forceSave(usr); // fixed?????????????????????
			// ??????????????????????????????
			passwords.put(userId, usr.getPassword());
		});
	}

	/**
	 * ???????????????????????????
	 *
	 * @param userId
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void resetPassword(String userId) {
		repository.findById(userId).ifPresent(usr -> {
			String resetPassword = encoder.encode(CoframeConstants.DEFAULT_USER_PASSWORD);
			usr.setPassword(resetPassword);
			passwords.put(userId, resetPassword);
			usr.setSalt(null); // ???????????????token ????????????
			repository.forceSave(usr); // fixed?????????????????????
		});
	}

	/**
	 * ????????????
	 *
	 * @param userId
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void enableUser(String userId) {
		repository.findById(userId).ifPresent(user -> {
			user.setStatus(CoframeConstants.STATUS_ENABLED);
			repository.forceSave(user); // fixed?????????????????????
			cacheManager.delete(cacheKey.userName2User(user.getName()));
		});
	}

	/**
	 * ????????????
	 *
	 * @param userId
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void disableUser(String userId) {
		repository.findById(userId).ifPresent(user -> {
			user.setStatus(CoframeConstants.STATUS_DISABLED);
			repository.forceSave(user); // fixed?????????????????????
			cacheManager.delete(cacheKey.userName2User(user.getName()));
		});
	}

	/**
	 * ???????????? id ????????????
	 *
	 * @param employeeId
	 * @return
	 */
	public User findByEmployeeId(String employeeId) {
		User.Criteria criteria = new User.Criteria();
		criteria.setEmployeeId(employeeId);
		Optional<User> user = repository.findOne(repository.toSpecification(criteria));
		return user.isPresent() ? user.get() : null;
	}

	/**
	 * ???????????????????????????
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
	 * ????????????????????????
	 */
	@Override
	public CofUser getUserByName(String userName) {
		String key = cacheKey.userName2User(userName); // userName -> user
		Optional<CofUser> cache = cacheManager.get(key, CofUser.class);
		if (cache.isPresent()) {
			return cache.get();
		}

		User user = findByUserName(userName);
		if (user != null) {
			CofUser cofUser = copyNotNullProperties(user, new CofUser());
			cacheManager.put(key, cofUser);
			return cofUser;
		}
		return null;
	}

	@Override
	public String getUserPassword(String userId) {
		// ??????????????????????????????
		String pwd = passwords.get(userId);
		// ????????????????????????????????????
		if (pwd == null) {
			pwd = findById(userId).getPassword();
			passwords.put(userId, pwd);
		}
		return pwd;
	}

	/**
	 * ?????????????????????
	 *
	 * @param userId
	 * @return
	 */
	public Set<Menu> listUserMenus(String userId, String ownerType, String ownerId) {
		if (StringUtils.isNotBlank(ownerType) && StringUtils.isNotBlank(ownerId)) {
			return repository.findUserMenusByOwner(userId, ownerType, ownerId);
		}
		return repository.findUserMenus(userId);
	}

	/**
	 * ????????????????????????????????????
	 *
	 * @param userId
	 * @return
	 */
	public Set<Function> listUserFunctions(String userId, String ownerType, String ownerId) {
		if (StringUtils.isNotBlank(ownerType) && StringUtils.isNotBlank(ownerId)) {
			return repository.findUserFunctionsByOwner(userId, ownerType, ownerId);
		}
		return repository.findUserFunctions(userId);
	}

	/**
	 * ???????????????????????????????????????
	 */
	public Set<String> getUserFuctionCodes(String userId, String ownerType, String ownerId) {
		return getUserFuctions(userId,ownerType,ownerId).stream().map(x -> x.getCode()).collect(Collectors.toSet());
	}

	public Set<CofFunction> getUserFuctions(String userId,String ownerType,String ownerId) {
		Set<CofFunction> cofFunctions = new HashSet<>();
		Set<Function> functions = listUserFunctions(userId, ownerType, ownerId);
		if (functions != null) {
			cofFunctions = functions.stream().map(x -> copyNotNullProperties(x, new CofFunction())).collect(Collectors.toSet());
		}
		return cofFunctions;
	}

	/**
	 * ????????????????????????
	 * 
	 * @param criteria
	 * @param pageable
	 * @return
	 */
	public Page<User> pagingByCriteria(User.Criteria criteria, Pageable pageable) {
		return repository.findAll(repository.toSpecification(criteria), pageable);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void updateSalt(String userId, String salt) {
		repository.findById(userId).ifPresent(user -> {
			user.setSalt(salt);
			repository.forceSave(user); // fixed???????????????salt
		});
	}

	@Override
	public CofUser getCurrentLoginedUser() {
		CofUser user = CofContext.getContext().get(CoframeConstants.CONTEXT_LOGINED_USER, CofUser.class);
		if (user == null) {
			throw CofErrorCode.USER_NOT_LOGIN.runtimeException();
		}
		return user;
	}

	@Override
	public void preUpdate(User user) {
		if (user.getName() != null) {
			repository.findById(user.getId()).ifPresent(findUser -> {
				if (!findUser.getName().equals(user.getName())) {
					throw CoframeErrorCode.USER_NAME_CHANGE_NOT_ALLOWED.runtimeException();
				}
			});
		}
		Employee employee = user.getEmployee();
		if (employee != null) {
			User.Criteria criteria = new User.Criteria();
			criteria.setEmployeeId(employee.getId());
			criteria.setId(user.getId());
			if (findByEmployeeId(employee.getId()) != null && repository.count(repository.toSpecification(criteria)) > 1) {
				throw CoframeErrorCode.EMPLOYEE_ALREADY_BIND_USER.runtimeException();
			}
		}
	}

	public Set<Role> listRoles(String userId) {
		return repository.listRoles(userId);
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public Set<CofRole> getUserRoles(String userId) {
		Set<Role> roles = listRoles(userId);
		return roles == null ? Collections.EMPTY_SET : roles.stream().map(x -> toCofRole(x)).collect(Collectors.toSet());
	}

	public CofRole toCofRole(Role role) {
		if (role == null) {
			return null;
		}

		// ????????????????????????
		String key = cacheKey.roleId2Role(role.getId());
		Optional<CofRole> cache = cacheManager.get(key, CofRole.class);
		if (cache.isPresent()) {
			return cache.get();
		}

		// ??????????????????????????????????????????
		CofRole cofRole = copyNotNullProperties(role, new CofRole());
		// ?????????????????????????????????
		List<Function> functions = roleRepository.getRoleFunctions(role.getId());
		if (functions != null) {
			cofRole.setFunctions(functions.stream().map(x -> copyNotNullProperties(x, new CofFunction())).collect(Collectors.toSet()));
		}
		cacheManager.put(key, cofRole);
		return cofRole;
	}

	@Override
	public CofRole getRole(String roleId) {
		// ????????????????????????
		String key = cacheKey.roleId2Role(roleId);
		Optional<CofRole> cache = cacheManager.get(key, CofRole.class);
		if (cache.isPresent()) {
			return cache.get();
		}

		// ???????????????
		return toCofRole(roleRepository.findById(roleId).get());
	}
}
