package org.gocom.coframe.auth.handler;

import static org.gocom.coframe.core.support.CoframePersistentModelEvent.Type.POST_CREATE;
import static org.gocom.coframe.core.support.CoframePersistentModelEvent.Type.POST_DELETE;

import org.gocom.coframe.CoframeConstants;
import org.gocom.coframe.core.support.CoframePersistentModelEvent;
import org.gocom.coframe.core.support.CoframePersistentModelEvent.Type;
import org.gocom.coframe.model.PartyAuth;
import org.gocom.coframe.model.Role;
import org.gocom.coframe.model.User;
import org.gocom.coframe.repository.RoleRepository;
import org.gocom.coframe.sdk.cache.CofCacheKey;
import org.gocom.coframe.sdk.cache.ICacheManager;
import org.gocom.coframe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 当认证相关的对象变更时，处理缓存的清除
 */
@Component
@ConditionalOnExpression("${coframe.cache.enable:true}")
public class CacheClearHandler {
	private Logger log = LoggerFactory.getLogger(CacheClearHandler.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleResitory;

	@Autowired
	private ICacheManager cacheManager;

	@Autowired
	private CofCacheKey cacheKey;

	@EventListener
	public void onEvent(CoframePersistentModelEvent<?> event) {
		Type type = event.getType();
		Object[] models = event.getModels();

		if (models != null) {
			for (Object model : models) {
				if (POST_CREATE == type) {
					handleModelCreate(model);
				} else if (POST_DELETE == type) {
					handleModelDelete(model);
				}
			}
		}
	}

	private void handleModelCreate(Object model) {
		// 用户与角色绑定
		if (model instanceof PartyAuth) {
			handlePartyAuthChange((PartyAuth) model);
		}
	}

	private void handleModelDelete(Object model) {
		// 用户删除
		if (model instanceof User) {
			User user = (User) model;
			cacheManager.delete(cacheKey.userName2User(user.getName()));
			log.info("User {} delete, clear cache", user.getName());
			return;
		}
		// 角色删除
		if (model instanceof Role) {
			Role role = (Role) model;
			Page<User> users = roleResitory.getUsersByRoleId(role.getId(), Pageable.unpaged());
			if (users != null && users.getContent() != null) {
				users.get().forEach(user -> {
					cacheManager.deleteWithPattern(cacheKey.userName2User(user.getName()));
				});
			}
			cacheManager.delete(cacheKey.roleId2Role(role.getId()));
			log.info("Role {} delete, clear cache", role.getName());
			return;
		}
		// 用户与角色解除绑定
		if (model instanceof PartyAuth) { // 新增用户与角色绑定, 清除用户角色缓存，用户权限码缓存
			handlePartyAuthChange((PartyAuth) model);
		}
	}

	private void handlePartyAuthChange(PartyAuth partyAuth) {
		if (CoframeConstants.PARTY_TYPE_USER.equals(partyAuth.getPartyType())) {
			String userId = partyAuth.getPartyId();
			User user = userService.findById(userId, false);
			if (user != null) {
				cacheManager.delete(cacheKey.userName2User(user.getName()));
			}
			log.info("User and role bind state changes, clear cache for user.id {}", userId);
		}
		if (CoframeConstants.AUTH_TYPE_ROLE.equals(partyAuth.getAuthType())) {
			cacheManager.delete(cacheKey.roleId2Role(partyAuth.getAuthId()));
		}
	}
}
